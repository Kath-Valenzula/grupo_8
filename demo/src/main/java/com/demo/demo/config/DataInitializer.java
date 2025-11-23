package com.demo.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.demo.models.User;
import com.demo.demo.repository.UserRepository;

@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            logger.info("=== INICIANDO CARGA DE DATOS PREDEFINIDOS ===");
            
            // Solo crear usuarios si no existen
            if (userRepository.count() == 0) {
                logger.info("Base de datos vacía. Creando usuarios predefinidos...");
                
                // Usuario 1: juanperez
                User user1 = new User();
                user1.setUsername("juanperez");
                user1.setEmail("juanperez@example.com");
                user1.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(user1);
                logger.info("Usuario creado: juanperez");
                
                // Usuario 2: admin
                User user2 = new User();
                user2.setUsername("admin");
                user2.setEmail("admin@example.com");
                user2.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(user2);
                logger.info("Usuario creado: admin");
                
                // Usuario 3: user
                User user3 = new User();
                user3.setUsername("user");
                user3.setEmail("user@example.com");
                user3.setPassword(passwordEncoder.encode("user123"));
                userRepository.save(user3);
                logger.info("Usuario creado: user");
                
                logger.info("=== CARGA DE DATOS COMPLETADA: 3 usuarios creados ===");
            } else {
                logger.info("La base de datos ya contiene {} usuarios. No se crean usuarios predefinidos.", userRepository.count());
            }
        };
    }
}
