package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartDetailDto> cartOrderDtoList; // 장바구니에서 여러 개의 상품을 주문하므로 CardOrderDto 클래스가 자기 자신을 List로 가짐
}
