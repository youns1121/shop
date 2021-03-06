package com.shop.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    EMAIL_NOT_FOUND(400, "M003", "로그인 후 이용해주세요"),


    // Data
    DATA_NOT_FOUND(400, "D0001", "정보를 찾을 수 없습니다"),

    // Order
    ORDER_NOT_FOUND(400, "O0001", "주문을 찾을 수 없습니다."),

    // Item
    ITEM_NOT_FOUND(400, "I0001", "존재하지 않는 상품입니다."),

    // Stock
    OUT_OF_STOCK(500, "S0001", "재고가 초과 되었습니다."),

    // Board
    BOARD_NOT_FOUND(400, "B0001", "존재하지 않는 게시물입니다."),

    // FILE
    FILE_NOT_FOUND(400, "FO001", "존재하지 않는 파일입니다."),

    ;
    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
