package com.shop.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInfoResponseDto {

    private List<OrderItemResponseListDto> orderItemResponseListDtoList;

    private OrderMemberInfoResponseDto orderMemberInfoResponseDto;

    private Integer orderSum;

    public OrderInfoResponseDto(List<OrderItemResponseListDto> orderItemResponseListDtoList, OrderMemberInfoResponseDto orderMemberInfoResponseDto, Integer orderSum) {
        this.orderItemResponseListDtoList = orderItemResponseListDtoList;
        this.orderMemberInfoResponseDto = orderMemberInfoResponseDto;
        this.orderSum = orderSum;
    }
}
