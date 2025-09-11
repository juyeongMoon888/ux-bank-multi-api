package com.mybank.multibank.controller;

import com.mybank.multibank.assembler.ExAccOperationMapper;
import com.mybank.multibank.domain.verification.VerificationResult;
import com.mybank.multibank.dto.*;
import com.mybank.multibank.global.ResponseUtil;
import com.mybank.multibank.global.code.SuccessCode;
import com.mybank.multibank.service.AccountService;
import com.mybank.multibank.service.ValidateService;
import com.mybank.multibank.service.transfer.model.ExternalHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/external-bank")
@RequiredArgsConstructor
public class ExternalAccountController {
    private final AccountService accountService;
    private final ResponseUtil responseUtil;
    private final ExternalHandler handler;
    private final ExAccOperationMapper mapper;

    @PostMapping("/open-account")
    public ResponseEntity<?> createAccount(@RequestBody ExternalOpenAccountRequestDto dto) {
        ExternalAccountOpenResponseDto account = accountService.createAccount(dto);

        return responseUtil.buildResponse(SuccessCode.ACCOUNT_CREATED, HttpStatus.OK, account);
    }

    @PostMapping("/transfer/verify")
    public VerificationResult verifyAccount(@RequestBody AccountVerifyRequestDto dto) {
        return accountService.validateAccount(dto);
    }

    @PostMapping("/deposit")
    public ExAccDepositRes deposit(@RequestBody ExAccDepositReq req) {
        ExAccOperationContext ctx = mapper.toDepositContext(req);
        return handler.deposit(ctx);
    }

    @PostMapping("/withdraw")
    public ExAccWithdrawRes withdraw(@RequestBody ExAccWithdrawReq req) {
        ExAccOperationContext ctx = mapper.toWithdrawContext(req);
        return handler.withdraw(ctx);
    }

    @PostMapping("/confirm")
    public ExAccConfirmRes confirm(@RequestBody ExAccConfirmReq req) {
        return handler.confirm(req);
    }

    @PostMapping("/cancel")
    public ExAccCancelRes cancel(@RequestBody ExAccCancelReq req) {
        return handler.cancel(req);
    }



}
