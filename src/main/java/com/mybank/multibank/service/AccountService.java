package com.mybank.multibank.service;

import com.mybank.multibank.application.command.TransactionCommandService;
import com.mybank.multibank.application.query.AccountQueryService;
import com.mybank.multibank.assembler.ExternalAccountAssembler;
import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.BankType;
import com.mybank.multibank.domain.TransactionStatus;
import com.mybank.multibank.domain.account.Account;
import com.mybank.multibank.domain.transaction.Transactions;
import com.mybank.multibank.domain.verification.VerificationResult;
import com.mybank.multibank.dto.AccountVerifyRequestDto;
import com.mybank.multibank.dto.ExternalOpenAccountRequestDto;
import com.mybank.multibank.dto.ExternalAccountOpenResponseDto;
import com.mybank.multibank.global.code.ErrorCode;
import com.mybank.multibank.global.exception.user.CustomException;
import com.mybank.multibank.domain.account.AccountRepository;
import com.mybank.multibank.service.transfer.model.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ExternalAccountAssembler externalAccountAssembler;

    public ExternalAccountOpenResponseDto createAccount(ExternalOpenAccountRequestDto dto) {
        BankType bankType;
        try {
            bankType = BankType.valueOf(dto.getBank().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.BANK_INVALID);
        }

        //계좌번호 중복 시 재시도 로직 넣기
        Account account = Account.builder()
                .bank(BankType.valueOf(dto.getBank()))
                .accountName(dto.getAccountName())
                .userId(dto.getUserId())
                .ownerName(dto.getName())
                .birth(dto.getBirth())
                .phone(dto.getPhone())
                .accountNumber(generateAccountNumber(bankType.getPrefix()))
                .status(AccountStatus.ACTIVE)
                .balance(0L)
                .build();
        Account savedAccount = accountRepository.save(account);

        return externalAccountAssembler.toResponseDto(savedAccount);
    }


    public VerificationResult validateAccount(AccountVerifyRequestDto dto) {
        return accountRepository.findStatusByBankAndAccountNumber(dto.getBank(), dto.getAccountNumber())
                .map(status -> switch (status) {
                    case ACTIVE -> VerificationResult.ok();
                    case FROZEN -> VerificationResult.frozen();
                    case CLOSED -> VerificationResult.closed();
                })
                .orElseGet(VerificationResult::notFound);
    }

    public String generateAccountNumber(String prefix) {
        String randomDigits = String.format("%08d", new Random().nextInt(100_100_100));
        return prefix + randomDigits;
    }
}
