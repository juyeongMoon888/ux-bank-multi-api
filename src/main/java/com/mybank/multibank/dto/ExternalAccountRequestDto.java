package com.mybank.multibank.dto;

import com.mybank.multibank.domain.AccountStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExternalAccountRequestDto {
    private final String bankCode;
    private final String accountNumber;
    private final String ownerName;
    private final Long balance;
}
