package com.mybank.multibank.repository;

import com.mybank.multibank.domain.ExternalAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalAccountRepository extends JpaRepository<ExternalAccount, Long> {
    Optional<ExternalAccount> findByBankTypeAndAccountNumber(String bankType, String accountNumber);
}
