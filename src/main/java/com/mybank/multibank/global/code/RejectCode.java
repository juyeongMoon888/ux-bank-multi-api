package com.mybank.multibank.global.code;

import org.springframework.http.HttpStatus;

public enum RejectCode implements BaseCode{
    ACCOUNT_CLOSED("reject.account_closed", HttpStatus.OK),
    ACCOUNT_FROZEN("reject.account_frozen", HttpStatus.OK)
    ;
    private final String message;
    private final HttpStatus httpStatus;

    RejectCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessageKey() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
