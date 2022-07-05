package com.shop.enums;

import lombok.Getter;
@Getter
public enum StatusEnum implements EnumModel{
    FLAG_Y("Y"),
    FLAG_N("N");

    StatusEnum(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    final String statusMessage;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return statusMessage;
    }
}
