package com.shop.entity;


import com.shop.dto.form.ItemFormDto;
import com.shop.entity.comm.BaseEntity;
import com.shop.enums.ItemSellStatus;
import lombok.*;

import javax.persistence.*;


@ToString
@Setter
@Getter
@Table(name="item")
@Entity
public class Item extends BaseEntity { /*상품*/

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @Column(name ="item_nm", nullable = false)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(name="stock_number", nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(name="item_detail", nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "item_sell_status")
    private ItemSellStatus itemSellStatus; //상품 판매상태



    public void updateItem(ItemFormDto itemFormDto){

        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getPrice();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}
