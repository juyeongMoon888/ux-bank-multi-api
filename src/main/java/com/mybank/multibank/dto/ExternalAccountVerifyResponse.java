package com.mybank.multibank.dto;

public class ExternalAccountVerifyResponse {
    private String code;
    private String message;
    private AccountData data;

    public static class AccountData {
        private String owner;
        private String status;
    }
}
