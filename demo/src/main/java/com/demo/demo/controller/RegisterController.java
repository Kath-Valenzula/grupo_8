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
            model.addAttribute("error", "Por favor corrige los errores en el formulario");
            return "register";
        }
        
        // Validar que el usuario no exista
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "register";
        }
        
        try {
            // Encriptar la contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Guardar el usuario en la base de datos
            userRepository.save(user);
            
            // Redirigir al login con mensaje de éxito
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "register";
        }
    }
}
