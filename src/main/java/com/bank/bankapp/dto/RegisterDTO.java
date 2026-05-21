package com.bank.bankapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    private String fullName;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @NotBlank
    private String phone;
}
