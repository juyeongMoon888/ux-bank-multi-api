package com.mybank.multibank.domain.account;

import com.mybank.multibank.domain.AccountStatus;
import com.mybank.multibank.domain.BankType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;


@Table(
    name = "external_accounts",
    uniqueConstraints = @UniqueConstraint(
            name = "uk_bank_account",
            columnNames = {"bank","account_number"}
    )
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

    @Setter
    private Long balance;

    private String accountName;
    private Long userId;
    private String ownerName;
    private String birth;
    private String phone;
    private String accountNumber;

    public void deposit(Long amount) {
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        if (balance < amount) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AccountStatus status;


}
