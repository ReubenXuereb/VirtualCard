package com.example.nium.virtualcard.api.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record AmountDto(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount
){}
