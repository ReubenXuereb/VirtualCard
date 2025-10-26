package com.example.nium.virtualcard.api.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record VirtualCardDto(
        @NotBlank
        String cardholderName,

        @DecimalMin(value = "0.00", inclusive = true)
        BigDecimal initialBalance
) {
    public VirtualCardDto(String cardholderName) {
        this(cardholderName, BigDecimal.ZERO);
    }
}

