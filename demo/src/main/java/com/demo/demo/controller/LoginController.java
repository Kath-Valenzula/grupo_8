package com.demo.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        logger.info("=== Acceso a página de login (error={}, logout={}, registered={}) ===", error, logout, registered);
        
        if (error != null) {
            logger.error("Error en login: credenciales incorrectas");
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            logger.info("Usuario cerró sesión");
            model.addAttribute("message", "Sesión cerrada exitosamente");
        }
        if (registered != null) {
            logger.info("Usuario registrado exitosamente");
            model.addAttribute("success", "Usuario registrado exitosamente. Ahora puedes iniciar sesión.");
        }
        return "login";
    }
}