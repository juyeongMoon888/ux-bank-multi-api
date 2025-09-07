package com.mybank.multibank.global.code;

import org.springframework.http.HttpStatus;

public enum RejectCode {
    ACCOUNT_CLOSED("reject.account.closed", HttpStatus.OK),
    ;
    private final String message;
    private final HttpStatus httpStatus;

    RejectCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
