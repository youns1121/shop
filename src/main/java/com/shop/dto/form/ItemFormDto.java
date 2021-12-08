package com.shop.dto.form;

import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.enums.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    private String itemNm; //아이템 이름름

    private Integer price;

    private String itemDetail;

    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        // modelMapper를 이용하여 엔티티 객체와 Dto 객체 간의 데이터를 복사하여 복사한 객체를 반환해주는 메소드
       return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
