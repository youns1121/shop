package com.shop.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemDto { //상품 상세페이지 장바구니 담기

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count;
}
