package com.shop.service;

import com.shop.domain.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${custom.path.itemImgLocation:D:/shop/item}")
    String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){

            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 호출 결과 로컬에 저장된 파일의 이름을 저장

            imgUrl = "/images/item/" + imgName; // 저장한 상품 이미지를 불러올 경로를 설정
        }

        //상품 이미지 정보 저장
        /**
         * imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
         * orilmgName : 업로드했던 상품 이미지 파일의 원래 이름
         * imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
         */

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ //상품 이미지를 수정한 경우 상품 이미지를 업데이트
            ItemImg savedItemimg = itemImgRepository.findById(itemImgId) // 상품 아이디를 활용하여 기존에 저장했던 상품 이미지 엔티티를 조회
                   .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemimg.getImgName())){ //기존에 등록된 상품 이미지 팡리이 있을 경우 해당 파일을 삭제합니다.
                fileService.deleteFile(itemImgLocation+"/"+ savedItemimg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 업데이트한 상품 이미지 파일을 업로드합니다.
            String imgUrl = "/images/item" + imgName;
            savedItemimg.updateItemImg(oriImgName, imgName, imgUrl); // 변경된 상품 이미지 정보를 세팅
        }
    }

}
