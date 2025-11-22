package com.demo.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHashPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hash = encoder.encode(password);
        System.out.println("\n===========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("===========================================");
        
        // Verificar que funciona
        boolean matches = encoder.matches(password, hash);
        System.out.println("Verificación correcta: " + matches);
        System.out.println("===========================================\n");
    }
}
