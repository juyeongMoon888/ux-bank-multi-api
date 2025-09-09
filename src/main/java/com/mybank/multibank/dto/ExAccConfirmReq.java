package com.mybank.multibank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExAccConfirmReq {
    private String bank;
    private long exTxId;
}