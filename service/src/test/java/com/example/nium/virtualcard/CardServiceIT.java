package com.example.nium.virtualcard;

import com.example.nium.virtualcard.config.ServiceTestConfig;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
import com.example.nium.virtualcard.core.model.AmountRequest;
import com.example.nium.virtualcard.core.model.CardStatus;
import com.example.nium.virtualcard.core.model.TransactionType;
import com.example.nium.virtualcard.core.repository.CardRepository;
import com.example.nium.virtualcard.core.repository.TransactionRepository;
import com.example.nium.virtualcard.core.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class CardServiceIT {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private Card card;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        transactionRepository.deleteAll();
        card = new Card("Test User", BigDecimal.valueOf(100), CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Test
    void testConcurrentSpending() throws InterruptedException {
        int threads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    cardService.spend(card.getId(), new AmountRequest(BigDecimal.valueOf(80)));
                } catch (OptimisticLockingFailureException | IllegalArgumentException e) {
                    System.out.println("Transaction failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        System.out.println("Final balance: " + updatedCard.getBalance());

        assertTrue(updatedCard.getBalance().compareTo(BigDecimal.ZERO) >= 0);

        List<Transaction> transactions = transactionRepository.findByCardIdOrderByCreatedAtDesc(card.getId());
        BigDecimal totalSpent = transactions.stream()
                .filter(t -> t.getType() == TransactionType.SPEND)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertTrue(totalSpent.compareTo(BigDecimal.valueOf(100)) <= 0);
    }
}

