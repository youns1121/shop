package com.shop.global.error.exception;

public class OrderNotFoundException extends BusinessException{

    public OrderNotFoundException(String message) {
        super(message, ErrorCode.ORDER_NOT_FOUND);
    }
}
