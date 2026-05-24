package com.bank.bankapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Indexed(unique = true)
    private String phone;

    private String role = "ROLE_USER";

    private LocalDateTime createdAt = LocalDateTime.now();
}
