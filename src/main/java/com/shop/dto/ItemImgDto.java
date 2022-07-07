package com.shop.dto;

import com.shop.domain.ItemImg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Builder
@Getter
@Setter
public class ItemImgDto { //상품 저장 후 상품 이미지에 대한 데이터를 전달할 DTO 클래스

    private Long id;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public static ItemImgDto from(ItemImg itemImg){

        return ItemImgDto.builder()
                .id(itemImg.getId())
                .oriImgName(itemImg.getOriImgName())
                .imgUrl(itemImg.getImgUrl())
                .repImgYn(itemImg.getRepImgYn())
                .build();
    }
}
