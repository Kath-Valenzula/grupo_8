package com.demo.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.demo.models.Receta;
import com.demo.demo.models.User;
import com.demo.demo.repository.RecetaRepository;
import com.demo.demo.repository.UserRepository;

@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RecetaRepository recetaRepository, PasswordEncoder passwordEncoder) {
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
            
            // Crear recetas de ejemplo si no existen
            if (recetaRepository.count() == 0) {
                logger.info("Creando recetas de ejemplo...");
                
                Receta receta1 = new Receta();
                receta1.setNombre("Tacos al Pastor");
                receta1.setIngredientes("Carne de cerdo, piña, cebolla, cilantro, tortillas");
                receta1.setTipoCocina("Mexicana");
                receta1.setPaisOrigen("México");
                receta1.setDificultad("Media");
                receta1.setInstrucciones("1. Marinar la carne. 2. Asar en trompo. 3. Servir en tortillas con piña y cebolla.");
                receta1.setTiempoCoccion(45);
                receta1.setPopular(true);
                recetaRepository.save(receta1);
                
                Receta receta2 = new Receta();
                receta2.setNombre("Pasta Carbonara");
                receta2.setIngredientes("Pasta, huevos, queso parmesano, panceta, pimienta negra");
                receta2.setTipoCocina("Italiana");
                receta2.setPaisOrigen("Italia");
                receta2.setDificultad("Fácil");
                receta2.setInstrucciones("1. Cocer la pasta. 2. Freír la panceta. 3. Mezclar con huevos y queso.");
                receta2.setTiempoCoccion(20);
                receta2.setPopular(true);
                recetaRepository.save(receta2);
                
                Receta receta3 = new Receta();
                receta3.setNombre("Paella Valenciana");
                receta3.setIngredientes("Arroz, pollo, conejo, judías verdes, garrofón, azafrán");
                receta3.setTipoCocina("Española");
                receta3.setPaisOrigen("España");
                receta3.setDificultad("Alta");
                receta3.setInstrucciones("1. Sofreír las carnes. 2. Añadir arroz y caldo. 3. Cocinar a fuego medio.");
                receta3.setTiempoCoccion(60);
                receta3.setPopular(true);
                recetaRepository.save(receta3);
                
                logger.info("=== 3 recetas de ejemplo creadas ===");
            } else {
                logger.info("La base de datos ya contiene {} recetas.", recetaRepository.count());
            }
        };
    }
}
