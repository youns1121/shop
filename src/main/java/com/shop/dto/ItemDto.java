package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private String ItemDetail;

    private String sellStatCd;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
