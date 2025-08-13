package com.mybank.multibank.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ExternalAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankType;
    private String bankName;
    private String accountNumber;
    private String ownerName;
    private String status;
    private Long balance;
}
