package com.mybank.multibank.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(
    name = "external_accounts",
    uniqueConstraints = @UniqueConstraint(
            name = "uk_bank_account",
            columnNames = {"bank_type","account_number"}
    ),
    indexes = @Index(name="idx_external_account_bank_type", columnList="bank_type")
)
@Getter
@Builder
public class ExternalAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String bankType;
    private String accountName;
    private Long userId;
    private String ownerName;
    private String birth;
    private String phone;
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AccountStatus status;

    private Long balance;
}
