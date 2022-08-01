package com.shop.dto.response;

import com.shop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailResponseDto {

    private Long orderId;

    private String memberName;

    private String memberEmail;

    private String memberAddress;

    private Integer orderCount;

    private Integer orderPrice;

    private String itemName;

    private String itemDetail;

    private LocalDateTime createTime;

    private OrderStatus orderStatus;

    private String orderStatusMessage;

    private String imgUrl;

    private String imgName;

    public String getOrderStatusMessage() {
        return orderStatus.getValue();
    }
}
