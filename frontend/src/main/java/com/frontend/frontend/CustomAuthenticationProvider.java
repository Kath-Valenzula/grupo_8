package com.frontend.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.frontend.frontend.model.LoginResponse;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final TokenStore tokenStore;
    private final RestTemplate restTemplate;

    @Value("${backend.api.base-url:http://localhost:8080}")
    private String backendBaseUrl;

    public CustomAuthenticationProvider(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        System.out.println("=== INICIANDO AUTENTICACIÓN ===");
        System.out.println("Usuario: " + name);
        System.out.println("Backend URL: " + backendBaseUrl + "/api/auth/login");

        String token;

        try {
            // Autenticar contra el backend usando la API JWT
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(
                Map.of("username", name, "password", password),
                headers
            );

            System.out.println("Enviando request al backend...");
            final var responseEntity = restTemplate.postForEntity(
                backendBaseUrl + "/api/auth/login",
                entity,
                LoginResponse.class
            );

            System.out.println("Response status: " + responseEntity.getStatusCode());
            final LoginResponse body = responseEntity.getBody();
            if (body == null || body.getToken() == null || body.getToken().isBlank()) {
                System.out.println("ERROR: Respuesta de login incompleta");
                throw new BadCredentialsException("Respuesta de login incompleta");
            }
            token = body.getToken();
            System.out.println("Token recibido: " + token.substring(0, Math.min(20, token.length())) + "...");
        } catch (RestClientException ex) {
            System.out.println("ERROR en autenticación: " + ex.getMessage());
            ex.printStackTrace();
            throw new BadCredentialsException("No se pudo autenticar: " + ex.getMessage());
        }

        tokenStore.setToken(token);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
