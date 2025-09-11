package com.mybank.multibank.assembler;

import com.mybank.multibank.domain.FlowContext;
import com.mybank.multibank.dto.ExAccDepositReq;
import com.mybank.multibank.dto.ExAccOperationContext;
import com.mybank.multibank.dto.ExAccWithdrawReq;
import org.springframework.stereotype.Component;

@Component
public class ExAccOperationMapper {
    public ExAccOperationContext toDepositContext (ExAccDepositReq req) {
        boolean isLeg = switch (FlowContext.valueOf(req.getFlow())) {
            case TRANSFER -> true;
            default -> false;
        };

        return ExAccOperationContext.builder()
                .idempotencyKey(req.getIdempotencyKey())
                .toBank(req.getToBank())
                .toAccountNumber(req.getToAccountNumber())
                .amount(req.getAmount())
                .memo(req.getMemo())
                .fromBank(isLeg ? req.getFromBank() : null)
                .fromAccountNumber(isLeg ? req.getFromAccountNumber() : null)
                .flow(FlowContext.valueOf(req.getFlow()))
                .build();
    }

    public ExAccOperationContext toWithdrawContext (ExAccWithdrawReq req) {
        boolean isLeg = switch (FlowContext.valueOf(req.getFlow())) {
            case TRANSFER -> true;
            default -> false;
        };

        return ExAccOperationContext.builder()
                .idempotencyKey(req.getIdempotencyKey())
                .toBank(isLeg ? req.getToBank() : null)
                .toAccountNumber(isLeg ? req.getToAccountNumber() : null)
                .amount(req.getAmount())
                .memo(req.getMemo())
                .fromBank(req.getFromBank())
                .fromAccountNumber(req.getFromAccountNumber())
                .flow(FlowContext.valueOf(req.getFlow()))
                .build();
    }

}
