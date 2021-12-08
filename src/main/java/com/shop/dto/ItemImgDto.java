package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto { //상품 저장 후 상품 이미지에 대한 데이터를 전달할 DTO 클래스

    private Long id;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); //멤버 변수로 ModelMapper 객체를 추가

    public static ItemImgDto of(ItemImg itemImg){
        /*ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을때 ItemImgDto로 값을 복사에서 반환*/
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
