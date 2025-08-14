package com.mybank.multibank.global.code;

import org.springframework.http.HttpStatus;

public enum SuccessCode implements BaseCode{
    ACCOUNT_VERIFIED("success.account_verified", HttpStatus.OK),
    ACCOUNT_CREATED("success.account_created", HttpStatus.OK)
    ;
    private final String messageKey;
    private final HttpStatus httpStatus;

    SuccessCode(String messageKey, HttpStatus httpStatus) {
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
