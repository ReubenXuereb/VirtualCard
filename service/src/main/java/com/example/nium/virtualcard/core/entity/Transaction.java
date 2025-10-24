package com.example.nium.virtualcard.core.entity;

import com.example.nium.virtualcard.core.entity.model.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_timestamp", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transaction() {}
    
    public Transaction(Long id, Card card, TransactionType type, BigDecimal amount) {
        this.id = id;
        this.card = card;
        this.type = type;
        this.amount = amount;
    }
}
