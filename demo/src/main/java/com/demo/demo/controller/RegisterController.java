package com.demo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.demo.models.User;
import com.demo.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, 
                          BindingResult result, 
                          Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        model.addAttribute("success", "Usuario registrado exitosamente");
        return "redirect:/login?registered=true";
    }
}
