package com.mybank.multibank.controller;

import com.mybank.multibank.domain.ExternalAccount;
import com.mybank.multibank.dto.ExternalAccountVerifyResponse;
import com.mybank.multibank.global.ResponseUtil;
import com.mybank.multibank.global.code.SuccessCode;
import com.mybank.multibank.service.ExternalAccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class ExternalAccountController {
    private final ExternalAccountService accountService;
    private final ResponseUtil responseUtil;

    @GetMapping("/validate")
    public ResponseEntity<?> validateAccount(
            @RequestParam String bankCode,
            @RequestParam String accountNumber) {
        ExternalAccountVerifyResponse ownerName = accountService.validateAccount(bankCode, accountNumber);
        return responseUtil.buildResponse(SuccessCode.ACCOUNT_VERIFIED, HttpStatus.OK, ownerName);
    }
}
