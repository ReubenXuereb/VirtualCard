package com.example.nium.virtualcard.core.repository;

import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCardOrderByCreatedAtDesc(Card card);

    List<Transaction> findByCardIdOrderByCreatedAtDesc(Long cardId);
}
