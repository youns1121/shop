package com.shop.global.error.exception;

public class OutOfStockException extends BusinessException{

    public OutOfStockException(String message){

        super(message, ErrorCode.OUT_OF_STOCK);

    }
}
