package com.bank.bankapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferDTO {

    @NotBlank
    private String toAccountNumber;

    @NotNull @DecimalMin("1.0")
    private BigDecimal amount;

    private String description;
}
