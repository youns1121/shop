package com.shop.dto.form;

import com.shop.dto.ItemImgDto;
import com.shop.domain.Item;
import com.shop.enums.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm; //아이템 이름

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>(); // 상품 이미지 아이디를 저장하는 리스트, 수정시에 이미지 아이디를 담아둘 용도

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        // modelMapper를 이용하여 엔티티 객체와 Dto 객체 간의 데이터를 복사하여 복사한 객체를 반환해주는 메소드
       return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); // modelMapper를 이용하여 엔티티 객체와 Dto 객체 간의 데이터를 복사하여 복사한 객체를 반횐해주는 메서드
    }
}
