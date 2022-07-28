package com.shop.enums;

import lombok.Getter;

@Getter
public enum BoardEnum implements EnumModel{
    ALL("전체"),
    TITLE("제목"),
    CONTENTS("내용"),
    WRITER("작성자"),
    ;

    final String statusMessage;

    BoardEnum(String statusMessage) {
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
