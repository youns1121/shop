package com.shop.enums;

public enum OrderStatus implements EnumModel{
    ORDER("주문"),
    ORDER_CANCEL("주문 취소");

    final String statusMessage;

    OrderStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return statusMessage;
    }
}
