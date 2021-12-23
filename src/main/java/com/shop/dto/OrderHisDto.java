package com.shop.dto;


import com.shop.entity.Order;
import com.shop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHisDto{ //주문 정보를 담을 Dto


    private Long orderId; //주문 아이디

    private String orderDate; //주문 날짜

    private OrderStatus orderStatus; //주문 상태

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto){ // orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드드

       orderItemDtoList.add(orderItemDto);
    }

    public OrderHisDto(Order order){ // 주문 날짜 포맷 수정

        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy--MM-dd HH:mm"));

    }


}
