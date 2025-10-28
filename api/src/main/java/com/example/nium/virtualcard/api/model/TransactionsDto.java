package com.example.nium.virtualcard.api.model;

import com.example.nium.virtualcard.core.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionsDto(
        Long id,
        Long cardId,
        TransactionType type,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
