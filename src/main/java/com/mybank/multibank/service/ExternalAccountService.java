package com.mybank.multibank.service;

import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.ExternalAccount;
import com.mybank.multibank.dto.ExternalAccountVerifyResponse;
import com.mybank.multibank.global.code.ErrorCode;
import com.mybank.multibank.global.exception.user.CustomException;
import com.mybank.multibank.repository.ExternalAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalAccountService {
    private ExternalAccountRepository accountRepository;

    public ExternalAccount findByBankTypeAndAccountNumberOrThrow(String bankCode, String accountNumber) {
        return accountRepository.findByBankTypeAndAccountNumber(bankCode, accountNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    public ExternalAccountVerifyResponse validateAccount(String bankCode, String accountNumber) {
        ExternalAccount account = findByBankTypeAndAccountNumberOrThrow(bankCode, accountNumber);

        return switch (account.getStatus()) {
            case ACTIVE -> new ExternalAccountVerifyResponse(account.getOwnerName());
            case SUSPENDED -> {
                log.warn("verify blocked: SUSPENDED account={}", account.getAccountNumber());
                throw new CustomException(ErrorCode.ACCOUNT_INACTIVE);
            }
            case CLOSED -> {
                log.warn("verify blocked: CLOSED account={}", account.getAccountNumber());
                throw new CustomException(ErrorCode.ACCOUNT_INACTIVE);
            }
            case FROZEN -> {
                log.warn("verify blocked: FROZEN account={}", account.getAccountNumber());
                throw new CustomException(ErrorCode.ACCOUNT_INACTIVE);
            }
        };
    }
}
