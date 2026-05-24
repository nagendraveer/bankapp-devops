package com.bank.bankapp.controller;

import com.bank.bankapp.entity.Account;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.service.AccountService;
import com.bank.bankapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Account account = accountService.getByEmail(userDetails.getUsername());
        User user = userService.findByEmail(userDetails.getUsername());
        var transactions = accountService.getTransactions(account.getId());

        model.addAttribute("account", account);
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions.stream().limit(5).toList());
        model.addAttribute("transactionCount", transactions.size());
        return "dashboard";
    }

    @GetMapping("/history")
    public String history(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Account account = accountService.getByEmail(userDetails.getUsername());
        model.addAttribute("account", account);
        model.addAttribute("transactions", accountService.getTransactions(account.getId()));
        return "history";
    }

    @GetMapping("/account")
    public String accountDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Account account = accountService.getByEmail(userDetails.getUsername());
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("account", account);
        model.addAttribute("user", user);
        return "account";
    }
}
