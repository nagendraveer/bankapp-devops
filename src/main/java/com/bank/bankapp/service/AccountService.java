package com.bank.bankapp.service;

import com.bank.bankapp.entity.Account;
import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.repository.AccountRepository;
import com.bank.bankapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account getByEmail(String email) {
        return accountRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccount_IdOrderByCreatedAtDesc(accountId);
    }

    @Transactional
    public void deposit(String email, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ONE) < 0)
            throw new RuntimeException("Minimum deposit is ₹1");

        Account account = getByEmail(email);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        saveTransaction(account, "DEPOSIT", amount, "Deposit", account.getBalance());
    }

    @Transactional
    public void withdraw(String email, BigDecimal amount) {
        Account account = getByEmail(email);
        if (amount.compareTo(BigDecimal.ONE) < 0)
            throw new RuntimeException("Minimum withdrawal is ₹1");
        if (account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        saveTransaction(account, "WITHDRAW", amount, "Withdrawal", account.getBalance());
    }

    @Transactional
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
        saveTransaction(from, "TRANSFER_OUT", amount, "Transfer to " + toAccountNumber + " - " + desc, from.getBalance());
        saveTransaction(to, "TRANSFER_IN", amount, "Transfer from " + from.getAccountNumber() + " - " + desc, to.getBalance());
    }

    private void saveTransaction(Account account, String type, BigDecimal amount, String description, BigDecimal balanceAfter) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setBalanceAfter(balanceAfter);
        transactionRepository.save(tx);
    }
}
