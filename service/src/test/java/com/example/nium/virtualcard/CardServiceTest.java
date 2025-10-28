package com.example.nium.virtualcard;

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
import com.example.nium.virtualcard.core.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card("Reuben", BigDecimal.valueOf(100), CardStatus.ACTIVE);
        card.setId(1L);
    }


    @Test
    void should_save_card_successfully_when_created() {
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(card, TransactionType.CREATE, BigDecimal.valueOf(100)));

        Card result = cardService.createCard(new CreateVirtualCardRequest(card.getCardholderName(), card.getBalance()));

        assertNotNull(result);
        assertEquals("Reuben", result.getCardholderName());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void should_return_card_when_card_exists() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardService.getCard(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Reuben", result.getCardholderName());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        assertEquals(CardStatus.ACTIVE, result.getStatus());
        verify(cardRepository, times(1)).findById(any());
    }

    @Test
    void should_return_CardNotFoundException_when_card_does_not_exist() {
        when(cardRepository.findById(3L)).thenThrow(CardNotFoundException.class);

        assertThrows(CardNotFoundException.class, () -> cardService.getCard(3L));
    }

    @Test
    void should_deduct_amount_when_having_sufficient_funds() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(card, TransactionType.SPEND, BigDecimal.valueOf(10)));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card result = cardService.spend(1L, new AmountRequest(BigDecimal.valueOf(10)));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(90), result.getBalance());
        verify(cardRepository, times(1)).findById(any());
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void should_throw_exception_when_having_insufficient_funds() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(InsufficientFundsException.class, () -> cardService.spend(1L, new AmountRequest(BigDecimal.valueOf(101))));
    }

    @Test
    void should_throw_exception_when_card_status_blocked() {
        card.setStatus(CardStatus.BLOCKED);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(CardBlockedException.class, () -> cardService.spend(1L, new AmountRequest(BigDecimal.valueOf(10))));
        assertThrows(CardBlockedException.class, () -> cardService.topUp(1L, new AmountRequest(BigDecimal.valueOf(10))));
    }

    @Test
    void should_increase_balance_when_topping_up() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(card, TransactionType.TOPUP, BigDecimal.valueOf(10)));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card result = cardService.topUp(1L,  new AmountRequest(BigDecimal.valueOf(10)));

        assertNotNull(result);
        assertEquals(result.getBalance(), BigDecimal.valueOf(110));
        verify(cardRepository, times(1)).findById(any());
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void should_get_all_transactions() {
        Transaction t1 = new Transaction(card, TransactionType.CREATE, BigDecimal.valueOf(100));
        Transaction t2 = new Transaction(card, TransactionType.SPEND, BigDecimal.valueOf(10));
        Transaction t3 = new Transaction(card, TransactionType.SPEND, BigDecimal.valueOf(40));
        Transaction t4 = new Transaction(card, TransactionType.TOPUP, BigDecimal.valueOf(50));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(transactionRepository.findByCardIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(t4,t3,t2,t1));

        List<Transaction> result = cardService.getTransactionHistory(1L);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(TransactionType.TOPUP, result.get(0).getType());

        verify(cardRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).findByCardIdOrderByCreatedAtDesc(1L);
    }






















}
