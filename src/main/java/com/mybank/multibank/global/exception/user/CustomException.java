package com.mybank.multibank.global.exception.user;

import com.mybank.multibank.global.code.ErrorCode;

public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
      super(errorCode.name());
      this.errorCode = errorCode;
    }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
