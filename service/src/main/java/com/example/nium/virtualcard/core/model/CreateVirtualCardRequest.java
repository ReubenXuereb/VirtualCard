package com.example.nium.virtualcard.core.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class CreateVirtualCardRequest {

    @NotBlank
    private String cardholderName;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialBalance = BigDecimal.ZERO;

    public CreateVirtualCardRequest(String cardholderName, BigDecimal initialBalance){
        this.cardholderName = cardholderName;
        this.initialBalance = initialBalance;
    }

    public String getCardHolderName() {
        return cardholderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardholderName = cardHolderName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
