package com.shop.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberRole implements EnumModel{
    USER("회원", "01"),
    ADMIN("관리자", "02");

    private final String key;

    private final String value;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
