package com.shop.service;

import com.shop.domain.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("C:/shop/images")
    String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Transactional
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {

        /**
         * imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
         * orilmgName : 업로드했던 상품 이미지 파일의 원래 이름
         * imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
         */

        String oriImgName = itemImgFile.getOriginalFilename();

        if(!StringUtils.hasText(oriImgName)){

            throw new FileNotFoundException("파일이 존재하지 않습니다.");
        }

        String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
        String imgUrl = "/shop/images/" + imgName; // 저장한 상품 이미지를 불러올 경로를 설정

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ //상품 이미지를 수정한 경우 상품 이미지를 업데이트
            ItemImg savedItemimg = itemImgRepository.findById(itemImgId) // 상품 아이디를 활용하여 기존에 저장했던 상품 이미지 엔티티를 조회
                   .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(StringUtils.hasText(savedItemimg.getImgName())){ //기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제합니다.
                fileService.deleteFile(itemImgLocation + "/"+ savedItemimg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 업데이트한 상품 이미지 파일을 업로드합니다.
            String imgUrl = "/shop/images/" + imgName;
            savedItemimg.updateItemImg(oriImgName, imgName, imgUrl); // 변경된 상품 이미지 정보를 세팅
        }
    }

    public String getFullPath(String filename){
        return itemImgLocation + "/" + filename;
    }

}
