package com.example.nium.virtualcard.core.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public record CreateVirtualCardRequest(
        @NotBlank
        String cardholderName,

        @DecimalMin(value = "0.00", inclusive = true)
        BigDecimal initialBalance
) {
    public CreateVirtualCardRequest(String cardholderName) {
        this(cardholderName, BigDecimal.ZERO);
    }
}