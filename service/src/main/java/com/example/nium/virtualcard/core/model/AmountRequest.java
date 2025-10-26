package com.example.nium.virtualcard.core.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public record AmountRequest(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount
){}
