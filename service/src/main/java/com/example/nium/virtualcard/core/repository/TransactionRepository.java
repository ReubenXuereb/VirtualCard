package com.example.nium.virtualcard.core.repository;

import com.example.nium.virtualcard.core.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
