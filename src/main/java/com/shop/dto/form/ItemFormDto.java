package com.shop.dto.form;

import com.shop.dto.ItemImgDto;
import com.shop.domain.Item;
import com.shop.enums.ItemSellStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName; //아이템 이름

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList; // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds; // 상품 이미지 아이디를 저장하는 리스트, 수정시에 이미지 아이디를 담아둘 용도

    @Builder
    public ItemFormDto(Long id, String itemName, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus, List<ItemImgDto> itemImgDtoList, List<Long> itemImgIds) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
        this.itemImgDtoList = itemImgDtoList;
        this.itemImgIds = itemImgIds;
    }

    public Item createItem(ItemFormDto itemFormDto){

        return Item.builder()
                .itemDetail(itemFormDto.getItemDetail())
                .itemName(itemFormDto.getItemName())
                .itemSellStatus(itemFormDto.getItemSellStatus())
                .stockNumber(itemFormDto.getStockNumber())
                .price(itemFormDto.getPrice())
                .build();
    }

    public static ItemFormDto of(Item item){
//        return modelMapper.map(item, ItemFormDto.class); // modelMapper를 이용하여 엔티티 객체와 Dto 객체 간의 데이터를 복사하여 복사한 객체를 반횐해주는 메서드

        return ItemFormDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .itemSellStatus(item.getItemSellStatus())
                .itemDetail(item.getItemDetail())
                .stockNumber(item.getStockNumber())
                .build();
    }
}
