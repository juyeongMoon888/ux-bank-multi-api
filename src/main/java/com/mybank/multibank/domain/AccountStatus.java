package com.mybank.multibank.domain;

public enum AccountStatus {
    ACTIVE, // 정상
    SUSPENDED, // 거래 정지
    CLOSED, // 해지
    FROZEN //압류/동결
}
