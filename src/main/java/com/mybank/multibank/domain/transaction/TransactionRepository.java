package com.mybank.multibank.domain.transaction;

import com.mybank.multibank.service.transfer.model.OperationType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Transactions> findById(long exTxId);

    Optional<Transactions> findByIdempotencyKeyAndOperationType(String idempotencyKey, OperationType operationType);
}
