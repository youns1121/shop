package com.shop.global.error.exception;

public class BoardNotFoundException extends BusinessException{

    public BoardNotFoundException(String message) {
        super(message, ErrorCode.BOARD_NOT_FOUND);
    }
}
