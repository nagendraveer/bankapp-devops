package com.bank.bankapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BankController {

    private int balance = 10000;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("balance", balance);
        return "index";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam int amount) {
        balance += amount;
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam int amount) {
        balance -= amount;
        return "redirect:/";
    }
}