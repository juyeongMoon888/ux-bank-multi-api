package com.mybank.multibank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Transactional
@Getter
@Setter
@NoArgsConstructor
public class Idempotency {
    @Id
    @Column(name="idempotency_key")
    private String key; // 클라이언트가 준 UUID
    private Long txId; // 매핑된 거래 ID
    private LocalDateTime createdAt;
}

