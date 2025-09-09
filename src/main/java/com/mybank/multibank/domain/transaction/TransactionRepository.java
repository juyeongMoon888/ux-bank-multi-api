package com.mybank.multibank.domain.transaction;

import com.mybank.multibank.service.transfer.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    Optional<Transactions> findByIdForUpdate(long exTxId);
}
