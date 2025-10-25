package com.example.nium.virtualcard.core.repository;

import com.example.nium.virtualcard.core.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
