package com.example.nium.virtualcard.core.service;

import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
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


}
