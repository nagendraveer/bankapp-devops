package com.bank.bankapp.repository;

import com.bank.bankapp.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByUserId(String userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
