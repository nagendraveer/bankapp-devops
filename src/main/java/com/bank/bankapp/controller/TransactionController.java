package com.bank.bankapp.controller;

import com.bank.bankapp.dto.TransferDTO;
import com.bank.bankapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam BigDecimal amount,
                          RedirectAttributes attr) {
        try {
            accountService.deposit(userDetails.getUsername(), amount);
            attr.addFlashAttribute("success", "₹" + amount + " deposited successfully!");
        } catch (RuntimeException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam BigDecimal amount,
                           RedirectAttributes attr) {
        try {
            accountService.withdraw(userDetails.getUsername(), amount);
            attr.addFlashAttribute("success", "₹" + amount + " withdrawn successfully!");
        } catch (RuntimeException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/transfer")
    public String transferPage(Model model) {
        model.addAttribute("transferDTO", new TransferDTO());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @ModelAttribute TransferDTO transferDTO,
                           RedirectAttributes attr) {
        try {
            accountService.transfer(
                    userDetails.getUsername(),
                    transferDTO.getToAccountNumber(),
                    transferDTO.getAmount(),
                    transferDTO.getDescription()
            );
            attr.addFlashAttribute("success", "₹" + transferDTO.getAmount() + " transferred successfully!");
        } catch (RuntimeException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
