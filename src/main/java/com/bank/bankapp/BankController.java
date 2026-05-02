package com.bank.bankapp;

import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {

    @GetMapping("/")
    public String home() {
        return "Bank App is Running!";
    }

    @GetMapping("/balance")
    public String balance() {
        return "Your balance is ₹10,000";
    }            // trigger CI
}