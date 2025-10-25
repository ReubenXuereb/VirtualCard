package com.example.nium.virtualcard.core.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long cardId) {
        super("Card with id:["+cardId+"] does not exist.");
    }
}
