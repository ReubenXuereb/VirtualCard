package com.example.nium.virtualcard.api.model;

import com.example.nium.virtualcard.core.model.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VirtualCardDetailsDto(
        Long id,
        String cardholderName,
        BigDecimal balance,
        LocalDateTime createdAt,
        CardStatus status
) {}

