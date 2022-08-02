package com.shop.dto.response;

import com.shop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemResponseListDto {

    private Long orderId;

    private Integer orderCount;

    private Integer orderPrice;

    private String itemName;

    private String itemDetail;

    private LocalDateTime createTime;

    private OrderStatus orderStatus;

    private String orderStatusMessage;

    private String imgUrl;

    private String imgName;

}
