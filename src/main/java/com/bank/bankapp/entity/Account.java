package com.bank.bankapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "accounts")
@Data
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    private String accountType = "SAVINGS";

    private String userId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
