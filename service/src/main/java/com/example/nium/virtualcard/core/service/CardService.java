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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CardService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Card createCard(CreateVirtualCardRequest createVirtualCardRequest) {
        Card newCard = new Card(
                createVirtualCardRequest.cardholderName(),
                createVirtualCardRequest.initialBalance(),
                CardStatus.ACTIVE
        );
        Card save = cardRepository.save(newCard);
        transactionRepository.save(new Transaction(save, TransactionType.CREATE, save.getBalance()));

        return newCard;
    }


    public Card getCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    @Transactional
    public Card spend(Long cardId, AmountRequest request) {
        Card card = getCard(cardId);

        if (card.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(cardId, request.amount());
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardBlockedException(cardId);
        }

        card.setBalance(card.getBalance().subtract(request.amount()));
        transactionRepository.save(new Transaction(card, TransactionType.SPEND, request.amount()));

        return cardRepository.save(card);
    }

    @Transactional
    public Card topUp(Long cardId, AmountRequest request) {
        Card card = getCard(cardId);

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardBlockedException(cardId);
        }

        card.setBalance(card.getBalance().add(request.amount()));
        transactionRepository.save(new Transaction(card, TransactionType.TOPUP, request.amount()));

        return cardRepository.save(card);
    }

    public List<Transaction> getTransactionHistory(Long cardId) {
        getCard(cardId);
        return transactionRepository.findByCardIdOrderByCreatedAtDesc(cardId);
    }
}
