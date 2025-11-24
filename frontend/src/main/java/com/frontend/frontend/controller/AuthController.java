package com.frontend.frontend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthController {

    @Value("${backend.api.base-url:http://localhost:8080}")
    private String backendBaseUrl;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @SuppressWarnings("null")
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("email", email);
            requestBody.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(
                backendBaseUrl + "/api/auth/register",
                HttpMethod.POST,
                entity,
                String.class
            );

            model.addAttribute("message", "Usuario registrado correctamente. Ahora puedes iniciar sesi\u00f3n.");
        } catch (org.springframework.web.client.RestClientException ex) {
            model.addAttribute("message", "Error al registrar usuario: " + ex.getMessage());
        }

        return "register";
    }
}
