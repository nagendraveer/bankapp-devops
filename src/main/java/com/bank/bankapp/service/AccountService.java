package com.bank.bankapp.service;

import com.bank.bankapp.entity.Account;
import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.AccountRepository;
import com.bank.bankapp.repository.TransactionRepository;
import com.bank.bankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public Account getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Transaction> getTransactions(String accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    public void deposit(String email, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ONE) < 0)
            throw new RuntimeException("Minimum deposit is ₹1");
        Account account = getByEmail(email);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        saveTransaction(account.getId(), "DEPOSIT", amount, "Deposit", account.getBalance());
    }

    public void withdraw(String email, BigDecimal amount) {
        Account account = getByEmail(email);
        if (amount.compareTo(BigDecimal.ONE) < 0)
            throw new RuntimeException("Minimum withdrawal is ₹1");
        if (account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        saveTransaction(account.getId(), "WITHDRAW", amount, "Withdrawal", account.getBalance());
    }

    public void transfer(String fromEmail, String toAccountNumber, BigDecimal amount, String description) {
        Account from = getByEmail(fromEmail);
        Account to = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));
        if (from.getAccountNumber().equals(toAccountNumber))
            throw new RuntimeException("Cannot transfer to your own account");
        if (from.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        String desc = description != null && !description.isBlank() ? description : "Transfer";
        saveTransaction(from.getId(), "TRANSFER_OUT", amount, "Transfer to " + toAccountNumber + " - " + desc, from.getBalance());
        saveTransaction(to.getId(), "TRANSFER_IN", amount, "Transfer from " + from.getAccountNumber() + " - " + desc, to.getBalance());
    }

    private void saveTransaction(String accountId, String type, BigDecimal amount, String description, BigDecimal balanceAfter) {
        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setBalanceAfter(balanceAfter);
        transactionRepository.save(tx);
    }
}
