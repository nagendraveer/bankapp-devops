package com.bank.bankapp.service;

import com.bank.bankapp.dto.RegisterDTO;
import com.bank.bankapp.entity.Account;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.AccountRepository;
import com.bank.bankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email already registered");
        if (userRepository.existsByPhone(dto.getPhone()))
            throw new RuntimeException("Phone already registered");

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        userRepository.save(user);

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(new BigDecimal("1000.00")); // welcome bonus
        account.setUser(user);
        accountRepository.save(account);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 9).toUpperCase();
    }
}
