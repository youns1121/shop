package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 장바구니 상세 내역
 */

@Getter
@Setter
public class CartDetailDto {

    private Long cartItemId; //장바구니 상품 아이디

    private String itemName; //상품명

    private int price; // 상품 금액

    private int count; // 수량

    private String imgUrl; // 상품 이미지 경로


    public CartDetailDto(Long cartItemId, String itemName, int price, int count, String imgUrl) { //장바구니 페이지에 전달할 데이터를 생성자의 파리미터로 만듬
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
