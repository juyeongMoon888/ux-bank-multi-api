package com.mybank.multibank.domain.account;

import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.BankType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Table(
    name = "external_accounts",
    uniqueConstraints = @UniqueConstraint(
            name = "uk_bank_account",
            columnNames = {"bank","account_number"}
    ),
    indexes = @Index(name="idx_external_account_bank", columnList="bank")
)
@Entity
@Getter
@Transactional
@Builder
@AllArgsConstructor
public class Account {
    protected Account() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank")
    private BankType bank;

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
