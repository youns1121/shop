package com.shop.service;

import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.form.ItemFormDto;
import com.shop.domain.Item;
import com.shop.domain.ItemImg;
import com.shop.enums.StatusEnum;
import com.shop.global.error.exception.ErrorCode;
import com.shop.global.error.exception.ItemNotFoundException;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    @Transactional
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {

        Item item = itemFormDto.createItem(itemFormDto);
        itemRepository.save(item);

        createItemImages(itemImgFileList, item);
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지를 등록 아이디 순으로 조회
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        addItemImgList(itemImgList, itemImgDtoList);

        Item item = getItem(itemId);

        ItemFormDto itemFormDto = ItemFormDto.from(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    @Transactional
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())// 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); // 상품 등록 화면으로 부터 전달받은 itemFormDto를 통해 상품 엔티티를 업데이트함

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 상품 이미지 아이디 리스트를 조회합니다.

        //이미지 등록
        updateItemImages(itemImgFileList, itemImgIds);

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    private void updateItemImages(List<MultipartFile> itemImgFileList, List<Long> itemImgIds) throws Exception {
        for(int i = 0; i < itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 상품 이미지를 업데이트하기 위해서 updateItemImg()
            //메서드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달
        }
    }

    private void createItemImages(List<MultipartFile> itemImgFileList, Item item) throws IOException {

        for(int i = 0; i< itemImgFileList.size(); i++){

            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            firstImgYn(i, itemImg);
            saveItemImg(itemImgFileList, i, itemImg);
        }
    }

    private void firstImgYn(int i, ItemImg itemImg) {

        if(i == 0){
            itemImg.updateRepImgYn(StatusEnum.FLAG_Y.getValue()); // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y" 세팅, 나머지 상품 이미지는 "N"으로 설정
        }else {
            itemImg.updateRepImgYn(StatusEnum.FLAG_N.getValue());
        }
    }

    private void saveItemImg(List<MultipartFile> itemImgFileList, int i, ItemImg itemImg) throws IOException {

        if(!itemImgFileList.get(i).isEmpty()) {
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품의 이미지 정보를 저장
        }
    }

    private void addItemImgList(List<ItemImg> itemImgList, List<ItemImgDto> itemImgDtoList) {

        for(ItemImg itemImg : itemImgList){

            itemImgDtoList.add(ItemImgDto.from(itemImg));
        }
    }

    private Item getItem(Long itemId) {

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }
}
