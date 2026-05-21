package com.bank.bankapp.repository;

import com.bank.bankapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser_Email(String email);
    Optional<Account> findByAccountNumber(String accountNumber);
}
