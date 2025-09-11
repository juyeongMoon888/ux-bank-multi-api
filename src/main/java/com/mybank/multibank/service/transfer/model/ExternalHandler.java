package com.mybank.multibank.service.transfer.model;

import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.BankType;
import com.mybank.multibank.domain.FlowContext;
import com.mybank.multibank.domain.TransactionStatus;
import com.mybank.multibank.domain.account.Account;
import com.mybank.multibank.domain.account.AccountRepository;
import com.mybank.multibank.domain.transaction.TransactionRepository;
import com.mybank.multibank.domain.transaction.Transactions;
import com.mybank.multibank.dto.*;
import com.mybank.multibank.global.code.ErrorCode;
import com.mybank.multibank.global.code.RejectCode;
import com.mybank.multibank.global.code.SuccessCode;
import com.mybank.multibank.global.exception.user.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExternalHandler {

    private final TransactionRepository txRepo;
    private final AccountRepository accRepo;

    public ExAccDepositRes deposit(ExAccOperationContext ctx) {
        //멱등: 이미 처리된 키면 성공 재응답 (중복 재시도 처리)
        Transactions existing = txRepo.findByIdempotencyKeyAndOperationType(
                ctx.getIdempotencyKey(), OperationType.DEPOSIT).orElse(null);
        if (existing != null) {
            return ExAccDepositRes.builder()
                    .success(true)
                    .code(SuccessCode.DEPOSIT_OK.name())
                    .message(SuccessCode.DEPOSIT_OK.getMessageKey())
                    .exTxId(existing.getId())
                    .build();
        }

        Account to = accRepo.findByBankAndAccountNumber(BankType.valueOf(ctx.getToBank()), ctx.getToAccountNumber())
                .orElseThrow(null);
        // 계좌 없음 → success=false
        if (to == null) {
            return ExAccDepositRes.builder()
                    .success(false)
                    .code(ErrorCode.ACCOUNT_NOT_FOUND.name())
                    .message(ErrorCode.ACCOUNT_NOT_FOUND.getMessageKey())
                    .build();
        }
        // 비지니스 거절 → success=false
        else if (to.getStatus().equals(AccountStatus.CLOSED)) {
            return ExAccDepositRes.builder()
                    .success(false)
                    .code(RejectCode.ACCOUNT_CLOSED.name())
                    .message(RejectCode.ACCOUNT_CLOSED.getMessageKey())
                    .build();
        }

        else if (to.getStatus().equals(AccountStatus.FROZEN)) {
            return ExAccDepositRes.builder()
                    .success(false)
                    .code(RejectCode.ACCOUNT_FROZEN.name())
                    .message(RejectCode.ACCOUNT_FROZEN.getMessageKey())
                    .build();
        }

        long before = to.getBalance();
        to.deposit(ctx.getAmount());

        if (ctx.getFlow().equals(FlowContext.SIMPLE)) {
            Transactions depositLeg = Transactions.builder()
                    .account(to)
                    .operationType(OperationType.DEPOSIT)
                    .toBank(ctx.getToBank())
                    .toAccountNumber(ctx.getToAccountNumber())
                    .amount(ctx.getAmount())
                    .balanceBefore(before)
                    .balanceAfter(to.getBalance())
                    .memo(ctx.getMemo())
                    .idempotencyKey(ctx.getIdempotencyKey())
                    .transactionStatus(TransactionStatus.COMPLETED)
                    .build();
            txRepo.save(depositLeg);

            return ExAccDepositRes.builder()
                    .success(true)
                    .code(SuccessCode.DEPOSIT_OK.name())
                    .message(SuccessCode.DEPOSIT_OK.getMessageKey())
                    .exTxId(depositLeg.getId())
                    .externalBank(depositLeg.getToBank())
                    .build();
        } else {
            Transactions depositLeg = Transactions.builder()
                    .account(to)
                    .operationType(OperationType.DEPOSIT)
                    .toBank(ctx.getToBank())
                    .toAccountNumber(ctx.getToAccountNumber())
                    .amount(ctx.getAmount())
                    .balanceBefore(before)
                    .balanceAfter(to.getBalance())
                    .memo(ctx.getMemo())
                    .idempotencyKey(ctx.getIdempotencyKey())
                    .fromBank(BankType.valueOf(ctx.getFromBank()))
                    .fromAccountNumber(ctx.getFromAccountNumber())
                    .transactionStatus(TransactionStatus.COMPLETED)
                    .build();
            txRepo.save(depositLeg);
            return ExAccDepositRes.builder()
                    .success(true)
                    .code(SuccessCode.DEPOSIT_OK.name())
                    .message(SuccessCode.DEPOSIT_OK.getMessageKey())
                    .exTxId(depositLeg.getId())
                    .build();
        }
    }

    public ExAccWithdrawRes withdraw(ExAccWithdrawReq req) {
        //멱등: 이미 처리된 키면 성공 재응답 (중복 재시도 처리)
        Transactions existing = txRepo.findByIdempotencyKeyAndOperationType(
                req.getIdempotencyKey(), OperationType.WITHDRAW).orElse(null);
        if (existing != null) {
            return ExAccWithdrawRes.builder()
                    .approved(true)
                    .code(SuccessCode.WITHDRAW_OK.name())
                    .message(SuccessCode.WITHDRAW_OK.getMessageKey())
                    .exTxId(existing.getId())
                    .build();
        }

        Account from = accRepo.findByBankAndAccountNumber(BankType.valueOf(req.getFromBank()), req.getFromAccountNumber())
                .orElseThrow(null);
        // 계좌 없음 → approved=false
        if (from == null) {
            return ExAccWithdrawRes.builder()
                    .approved(false)
                    .code(ErrorCode.ACCOUNT_NOT_FOUND.name())
                    .message(ErrorCode.ACCOUNT_NOT_FOUND.getMessageKey())
                    .build();
        }
        // 비지니스 거절 → approved=false
        else if (from.getStatus().equals(AccountStatus.CLOSED)) {
            return ExAccWithdrawRes.builder()
                    .approved(false)
                    .code(RejectCode.ACCOUNT_CLOSED.name())
                    .message(RejectCode.ACCOUNT_CLOSED.getMessageKey())
                    .build();
        }

        else if (from.getStatus().equals(AccountStatus.FROZEN)) {
            return ExAccWithdrawRes.builder()
                    .approved(false)
                    .code(RejectCode.ACCOUNT_FROZEN.name())
                    .message(RejectCode.ACCOUNT_FROZEN.getMessageKey())
                    .build();
        }

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

    @Transactional
    public ExAccConfirmRes confirm(ExAccConfirmReq req) {
        //확인 플래그만
        Transactions tx = txRepo.findById(req.getExTxId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_TX_NOT_FOUND));

        //이미 CONFIRMED 상태
        if (tx.isPartnerConfirmed()) {
            return ExAccConfirmRes.builder().complete(true).successCode("ALREADY_CONFIRMED").build();
        }
        tx.setPartnerConfirmed(true);          // 메타만 갱신
        txRepo.save(tx);

        return ExAccConfirmRes.builder().complete(true).successCode("CONFIRMED").build();
    }

    @Transactional
    public ExAccCancelRes cancel(ExAccCancelReq req) {
        // 1) 출금 레그 (락) 조회
        Transactions txW = txRepo.findById(req.getExTxId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_TX_NOT_FOUND));

        // 2) 타입/상태 검증
        if (txW.getOperationType() != OperationType.WITHDRAW) {
            throw new IllegalArgumentException("ErrorCode.INVALID_TX_TYPE");
        }

        if (Boolean.TRUE.equals(txW.isPartnerConfirmed())) {
            //파트너 확정 후 취소 금지
            throw new IllegalArgumentException("ErrorCode.ALREADY_CONFIRM");
        }

        if (txW.getTransactionStatus() == TransactionStatus.CANCELED) {
            return ExAccCancelRes.builder()
                    .canceled(true)
                    .code("ALREADY_CANCELED")
                    .build();
        }

        // 3) 계좌 조회 (락) 레그 기준
        Account from = accRepo.findByBankAndAccountNumber(BankType.valueOf(txW.getFromBank().name()), txW.getFromAccountNumber())
                .orElseThrow( () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 4) 잔액 보상
        long before = from.getBalance();
        long after = before + txW.getAmount();
        from.setBalance(after);

        // 5) 보상 레그 생성
        Transactions refund = Transactions.builder()
                .account(from)
                .operationType(OperationType.REFUND)
                .toBank(txW.getToBank())
                .toAccountNumber(txW.getToAccountNumber())
                .amount(txW.getAmount())
                .balanceBefore(before)
                .balanceAfter(after)
                .memo("refund for tx#" + txW.getId())
                .idempotencyKey(txW.getIdempotencyKey())
                .fromAccountNumber(txW.getFromAccountNumber())
                .fromBank(txW.getFromBank())
                .transactionStatus(TransactionStatus.COMPLETED)
                .exTxId(txW.getId())
                .build();
        txRepo.save(refund);
        txRepo.flush();

        //6) 성공 응답
        return ExAccCancelRes.builder()
                .canceled(true)
                .code("CANCELED")
                .build();
    }

}
