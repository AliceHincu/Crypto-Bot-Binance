package com.example.cryptobot.repository;

import com.example.cryptobot.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Transaction, Integer> {
}
