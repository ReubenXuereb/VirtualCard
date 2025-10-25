package com.example.nium.virtualcard.core.exceptions;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(Long cardId) {
        super("Card with id:["+cardId+"] is Blocked. No further process.");
    }
}
