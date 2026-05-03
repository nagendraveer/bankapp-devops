package com.bank.bankapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BankController {

    private int balance = 10000;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("balance", balance);
        return "index";
    }

    @GetMapping("/api/balance")
    @ResponseBody
    public int getBalance() {
       return balance;
}

   @PostMapping("/deposit")
public String deposit(@RequestParam int amount, RedirectAttributes attr) {
    balance += amount;
    attr.addFlashAttribute("message", "Deposit successful!");
    return "redirect:/";
}

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam int amount) {
        balance -= amount;
        return "redirect:/";
    }
}