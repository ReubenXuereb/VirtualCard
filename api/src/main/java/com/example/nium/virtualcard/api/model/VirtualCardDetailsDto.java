package com.example.nium.virtualcard.api.model;

import com.example.nium.virtualcard.core.model.CardStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VirtualCardDetailsDto {
    private Long id;

    private String cardholderName;

    private BigDecimal balance;

    private LocalDateTime createdAt;

    private CardStatus status;

    public VirtualCardDetailsDto(Long id, String cardholderName, BigDecimal balance, LocalDateTime createdAt, CardStatus status) {
        this.id = id;
        this.cardholderName = cardholderName;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CardStatus getStatus() {
        return status;
    }
}
