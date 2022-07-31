package com.shop.global.error.exception;

public class EmailNotFoundException extends BusinessException{

    public EmailNotFoundException(String message) {
        super(message, ErrorCode.EMAIL_NOT_FOUND);
    }
}
