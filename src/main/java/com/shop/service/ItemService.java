package com.shop.service;

import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.form.ItemFormDto;
import com.shop.domain.Item;
import com.shop.domain.ItemImg;
import com.shop.enums.StatusEnum;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {

        //상품등록
        Item item = itemFormDto.createItem(itemFormDto); // 상품 등록 폼으로부터 입력받은 데이터를 이용하여 item 객체를 생성
        itemRepository.save(item); // 상품 데이터를 저장

        //이미지등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0){
                itemImg.setRepImgYn(StatusEnum.FLAG_Y.getValue()); // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y" 세팅, 나머지 상품 이미지는 "N"으로 설정
            }else {
                itemImg.setRepImgYn(StatusEnum.FLAG_N.getValue());
            }

            if(!itemImgFileList.get(i).isEmpty()) {
                itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품의 이미지 정보를 저장
            }
        }
        return item.getId();
    }

    @Transactional(readOnly = true) // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용 설정, jpa가 변경감지(더티체킹)을 수행하지 않아서 성능을 향상 시킬 수 있음
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지를 등록 아이디 순으로 조회

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for(ItemImg itemImg : itemImgList){ // 조회한 Itemimg 엔티티를 ItemImgDto객체로 만들어서 리스트에 추가

            itemImgDtoList.add(ItemImgDto.from(itemImg));
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); // 상품의 아이디를 통해 상품 엔티티를 조회합니다.
        ItemFormDto itemFormDto = ItemFormDto.of(item);
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
        for(int i = 0; i < itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 상품 이미지를 업데이트하기 위해서 updateItemImg()
            //메서드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달
        }

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
}
