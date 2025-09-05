package com.mybank.multibank.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    Optional<Transactions> findByIdAndAccount_Owner_Id(Long transactionId, Long userId);

    Optional<Transactions> findByIdForUpdate(long exTxId);
}
