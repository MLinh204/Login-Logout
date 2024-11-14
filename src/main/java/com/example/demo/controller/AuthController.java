package com.example.demo.controller;

import com.example.demo.DTO.UserRegistrationDTO;
import com.example.demo.model.User;
import com.example.demo.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AuthController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDTO userDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "register";
        }

        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            model.addAttribute("allRoles", userService.getAllRoles());
            return "register";
        }

        try {
            userService.registerNewUser(userDTO);
            model.addAttribute("success", "Registration completed successfully!");
            model.addAttribute("allRoles", userService.getAllRoles());
            return "register";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allRoles", userService.getAllRoles());
            return "register";
        }
    }
    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

}
