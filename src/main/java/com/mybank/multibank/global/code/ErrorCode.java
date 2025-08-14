package com.mybank.multibank.global.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode{
    ACCOUNT_NOT_FOUND("error.account_not_found", HttpStatus.NOT_FOUND),
    ACCOUNT_INACTIVE("error.account_inactive", HttpStatus.BAD_REQUEST),
    BANK_INVALID("error.bank_invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_DUPLICATE("error.account_duplicate", HttpStatus.CONFLICT),
    ;

    private final String messageKey;
    private final HttpStatus httpStatus;

    ErrorCode(String messageKey, HttpStatus httpStatus) {
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
