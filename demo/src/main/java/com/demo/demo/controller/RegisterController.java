package com.demo.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

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
        logger.info("=== INICIO REGISTRO ===");
        logger.info("Usuario recibido: {}", user.getUsername());
        logger.info("Email recibido: {}", user.getEmail());
        
        if (result.hasErrors()) {
            logger.error("Errores de validación: {}", result.getAllErrors());
            model.addAttribute("error", "Por favor corrige los errores en el formulario");
            return "register";
        }
        
        // Validar que el usuario no exista
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warn("Usuario {} ya existe", user.getUsername());
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "register";
        }
        
        try {
            logger.info("Encriptando contraseña...");
            // Encriptar la contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            logger.info("Guardando usuario en base de datos...");
            // Guardar el usuario en la base de datos
            User savedUser = userRepository.save(user);
            logger.info("Usuario guardado exitosamente con ID: {}", savedUser.getId());
            
            // Redirigir al login con mensaje de éxito
            logger.info("=== REGISTRO EXITOSO ===");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            logger.error("Error al registrar usuario: ", e);
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "register";
        }
    }
}
