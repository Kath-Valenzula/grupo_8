package com.frontend.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.frontend.frontend.config.BackendProperties;
import com.frontend.frontend.model.LoginResponse;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final TokenStore tokenStore;
    private final RestTemplate restTemplate;
    private final BackendProperties backendProperties;

    public CustomAuthenticationProvider(TokenStore tokenStore, BackendProperties backendProperties) {
        this.tokenStore = tokenStore;
        this.restTemplate = new RestTemplate();
        this.backendProperties = backendProperties;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        String token;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(
                Map.of("username", name, "password", password),
                headers
            );

            final var responseEntity = restTemplate.postForEntity(
                backendProperties.getBaseUrl() + "/api/auth/login",
                entity,
                LoginResponse.class
            );

            final LoginResponse body = responseEntity.getBody();
            if (body == null || body.getToken() == null || body.getToken().isBlank()) {
                throw new BadCredentialsException("Respuesta de login incompleta");
            }
            token = body.getToken();
            log.debug("Token recibido para usuario {} ({} chars)", name, token.length());
        } catch (RestClientException ex) {
            log.warn("Error autenticando contra backend: {}", ex.getMessage());
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
