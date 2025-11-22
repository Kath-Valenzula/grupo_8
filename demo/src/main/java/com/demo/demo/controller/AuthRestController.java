package com.demo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.dto.LoginRequest;
import com.demo.demo.dto.LoginResponse;
import com.demo.demo.models.User;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.security.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint de login que retorna un token JWT
     * 
     * POST /api/auth/login
     * Body: { "username": "juanperez", "password": "password123" }
     * Response: { "token": "eyJhbGc...", "type": "Bearer", "username": "juanperez", "email": "juan@..." }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, 
                                    BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + result.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            // Autenticar usuario con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // Obtener detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generar token JWT
            String jwt = jwtUtil.generateToken(userDetails);
            
            // Obtener información adicional del usuario
            User user = userRepository.findByUsername(userDetails.getUsername());
            
            // Retornar respuesta con token
            LoginResponse response = new LoginResponse(jwt, user.getUsername(), user.getEmail());
            
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas. Usuario o contraseña inválidos.");
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Datos inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
