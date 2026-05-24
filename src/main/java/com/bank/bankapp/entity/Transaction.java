package com.bank.bankapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
public class Transaction {

    @Id
    private String id;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT

    private BigDecimal amount;

    private String description;

    private BigDecimal balanceAfter;

    private String accountId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
