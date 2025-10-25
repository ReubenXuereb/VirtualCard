package com.example.nium.virtualcard.api.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class VirtualCardDto {
    @NotBlank
    private String cardholderName;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialBalance = BigDecimal.ZERO;

    public VirtualCardDto(String cardholderName, BigDecimal initialBalance){
        this.cardholderName = cardholderName;
        this.initialBalance = initialBalance;
    }

    public String getCardHolderName() {
        return cardholderName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

}
