package com.mybank.multibank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExAccDepositReq {
    private String toBank;
    private String toAccountNumber;
    private Long amount;
    private String memo;
    private String idempotencyKey;
    private String fromBank;
    private String fromAccountNumber;
}
