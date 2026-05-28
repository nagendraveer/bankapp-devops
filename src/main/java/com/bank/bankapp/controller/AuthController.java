package com.bank.bankapp.controller;

import com.bank.bankapp.dto.RegisterDTO;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.UserRepository;
import com.bank.bankapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin/setup")
    public String setupAdmin() {
        if (userRepository.findByEmail("admin@bankapp.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail("admin@bankapp.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("0000000000");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDTO registerDTO,
                           BindingResult result,
                           RedirectAttributes attr,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(registerDTO);
            attr.addFlashAttribute("success", "Account created! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
