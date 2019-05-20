package com.yqh.marketing.service.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private String messageCode;
    public BusinessException(HttpStatus internalServerError, String 手机号码格式不正确){

    }
    public BusinessException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }
}
