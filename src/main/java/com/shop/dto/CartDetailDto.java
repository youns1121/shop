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
}
