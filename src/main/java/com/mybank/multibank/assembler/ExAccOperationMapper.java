package com.mybank.multibank.assembler;

import com.mybank.multibank.domain.FlowContext;
import com.mybank.multibank.dto.ExAccDepositReq;
import com.mybank.multibank.dto.ExAccOperationContext;
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

}
