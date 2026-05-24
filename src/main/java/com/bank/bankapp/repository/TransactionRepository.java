package com.bank.bankapp.repository;

import com.bank.bankapp.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(String accountId);
}
