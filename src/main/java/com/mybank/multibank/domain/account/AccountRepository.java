package com.mybank.multibank.domain.account;

import com.mybank.multibank.domain.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByBankAndAccountNumber(String bank, String accountNumber);

    Optional<Account> findByAccountNumberAndUserId(String accountNumber, Long userId);
    @Query("""
           select a.status
             from Account a
            where a.bank = :bank
              and a.accountNumber = :accountNumber
           """)
    Optional<AccountStatus> findStatusByBankAndAccountNumber(@Param("bank") String bank,
                                                             @Param("accountNumber") String accountNumber);

}
