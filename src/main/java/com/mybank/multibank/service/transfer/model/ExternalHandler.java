package com.mybank.multibank.service.transfer.model;

import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.BankType;
import com.mybank.multibank.domain.Idempotency;
import com.mybank.multibank.domain.TransactionStatus;
import com.mybank.multibank.domain.account.Account;
import com.mybank.multibank.domain.account.AccountRepository;
import com.mybank.multibank.domain.transaction.TransactionRepository;
import com.mybank.multibank.domain.transaction.Transactions;
import com.mybank.multibank.dto.*;
import com.mybank.multibank.global.code.ErrorCode;
import com.mybank.multibank.global.code.SuccessCode;
import com.mybank.multibank.global.exception.user.CustomException;
import com.mybank.multibank.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalHandler {

    private final TransactionRepository txRepo;
    private final AccountRepository accRepo;

    public ExAccWithdrawRes withdraw(ExAccWithdrawReq req) {
        Account from = accRepo.findByBankAndAccountNumber(req.getFromBank(), req.getFromAccountNumber())
                .orElseThrow(null);

        long before = from.getBalance();
        from.withdraw(req.getAmount());

        Transactions withdrawLeg = Transactions.builder()
                .account(from)
                .operationType(OperationType.WITHDRAW)
                .toBank(req.getToBank())
                .toAccountNumber(req.getToAccountNumber())
                .amount(req.getAmount())
                .balanceBefore(before)
                .balanceAfter(from.getBalance())
                .memo(req.getMemo())
                .idempotencyKey(req.getIdempotencyKey())
                .fromBank(BankType.valueOf(req.getFromBank()))
                .fromAccountNumber(req.getFromAccountNumber())
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();
        txRepo.save(withdrawLeg);

        return ExAccWithdrawRes.builder()
                .approved(true)
                .code(SuccessCode.WITHDRAW_OK.name())
                .message(SuccessCode.WITHDRAW_OK.getMessageKey())
                .exTxId(withdrawLeg.getId())
                .build();
    }

    public ExAccConfirmRes confirm(ExAccConfirmReq req) {
        //확인 플래그만
        Transactions tx = txRepo.findByIdForUpdate(req.getExTxId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_TX_NOT_FOUND));

        if (tx.isPartnerConfirmed()) {
            return ExAccConfirmRes.builder().complete(true).successCode("ALREADY_CONFIRMED").build();
        }
        tx.setPartnerConfirmed(true);          // 메타만 갱신
        txRepo.save(tx);

        return ExAccConfirmRes.builder().complete(true).successCode("CONFIRMED").build();
    }

}
