package com.example.nium.virtualcard.core.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(Long cardId, BigDecimal amount) {
        super("Insufficient funds on card:["+cardId+"] for amount:["+amount+"]");
    }
}
