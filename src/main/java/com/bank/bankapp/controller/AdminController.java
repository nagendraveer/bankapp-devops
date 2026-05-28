package com.bank.bankapp.controller;

import com.bank.bankapp.entity.Account;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.AccountRepository;
import com.bank.bankapp.repository.TransactionRepository;
import com.bank.bankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userRepository.findAll();
        List<Account> accounts = accountRepository.findAll();

        long totalUsers = users.stream().filter(u -> "ROLE_USER".equals(u.getRole())).count();
        long totalAccounts = accounts.size();
        long totalTransactions = transactionRepository.count();
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        return "admin";
    }
}
