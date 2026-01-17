package com.eadl.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.backend.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountAccountNumber(String accountNumber);

    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId);
}