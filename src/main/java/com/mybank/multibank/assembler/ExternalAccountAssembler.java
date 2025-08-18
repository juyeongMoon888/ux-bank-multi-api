package com.mybank.multibank.assembler;

import com.mybank.multibank.domain.ExternalAccount;
import com.mybank.multibank.dto.ExternalAccountOpenResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ExternalAccountAssembler {
    public ExternalAccountOpenResponseDto toResponseDto(ExternalAccount entity) {
        return ExternalAccountOpenResponseDto.builder()
                .userId(entity.getUserId())
                .bankType(entity.getBankType())
                .accountNumber(entity.getAccountNumber())
                .accountName(entity.getAccountName())
                .externalAccountId(String.valueOf(entity.getId()))
                .balance(entity.getBalance())
                .build();
    }
}
