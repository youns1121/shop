package com.shop.dto;

import com.shop.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto { // 조회한 주문 데이터를 화면에 보낼 때 사용

    private String itemName; //상품명

    private int count; // 주문 수량

    private int orderPrice; //주문 금액

    private String imgUrl; // 상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl) { // OrderItem 객체와 이미지 경로를 파라미터로 담아 멤버 변수값을 세팅
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
