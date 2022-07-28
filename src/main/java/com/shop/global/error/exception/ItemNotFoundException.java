package com.shop.global.error.exception;

public class ItemNotFoundException extends BusinessException{

    public ItemNotFoundException(String message) {
        super(message, ErrorCode.ITEM_NOT_FOUND);
    }
}
