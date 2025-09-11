package com.mybank.multibank.dto;

import com.mybank.multibank.domain.FlowContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExAccOperationContext {
    private String toBank;
    private String toAccountNumber;
    private Long amount;
    private String memo;
    private String idempotencyKey;
    private String fromBank;
    private String fromAccountNumber;
    private FlowContext flow;
}
