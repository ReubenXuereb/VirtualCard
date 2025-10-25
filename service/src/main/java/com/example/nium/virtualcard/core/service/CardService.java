package com.example.nium.virtualcard.core.service;

import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
import com.example.nium.virtualcard.core.exceptions.CardBlockedException;
import com.example.nium.virtualcard.core.exceptions.CardNotFoundException;
import com.example.nium.virtualcard.core.exceptions.InsufficientFundsException;
import com.example.nium.virtualcard.core.model.AmountRequest;
import com.example.nium.virtualcard.core.model.CardStatus;
import com.example.nium.virtualcard.core.model.CreateVirtualCardRequest;
import com.example.nium.virtualcard.core.model.TransactionType;
import com.example.nium.virtualcard.core.repository.CardRepository;
import com.example.nium.virtualcard.core.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    private final CardRepository cardRepository;
    @Autowired
    private final TransactionRepository transactionRepository;

    public CardService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Card createCard(CreateVirtualCardRequest createVirtualCardRequest) {
        Card newCard = new Card(
                createVirtualCardRequest.getCardHolderName(),
                createVirtualCardRequest.getInitialBalance(),
                CardStatus.ACTIVE
        );
        Card save = cardRepository.save(newCard);
        transactionRepository.save(new Transaction(save, TransactionType.CREATE, save.getBalance()));

        return newCard;
    }


    @Transactional
    public Card getCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    @Transactional
    public Card spend(Long cardId, AmountRequest request) {
        Card card = getCard(cardId);

        if (card.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(cardId, request.getAmount());
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardBlockedException(cardId);
        }

        card.setBalance(card.getBalance().subtract(request.getAmount()));
        transactionRepository.save(new Transaction(card, TransactionType.SPEND, request.getAmount()));

        return card;
    }
}
