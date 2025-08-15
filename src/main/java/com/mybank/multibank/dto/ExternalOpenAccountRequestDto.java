package com.mybank.multibank.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExternalOpenAccountRequestDto {
    private final String bank;
    private final String password;
    private final String accountName;
    private final Long userId;
    private final String name;
    private final String birth;
    private final String phone;
}
