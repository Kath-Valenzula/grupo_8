package com.frontend.frontend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


    @Component
    public class CustomAuthenticationProvider implements AuthenticationProvider {

        private final TokenStore tokenStore;

        public CustomAuthenticationProvider(TokenStore tokenStore) {
            super();
            this.tokenStore = tokenStore;
        }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", name);
        requestBody.add("password", password);

        final var restTemplate = new RestTemplate();
        String token;

        try {
            final var responseEntity = restTemplate.postForEntity("http://localhost:8080/login", requestBody, String.class);
            final String body = responseEntity.getBody();
            if (body == null) {
                throw new BadCredentialsException("Respuesta vacía del servidor");
            }
            token = body.replace("{\"token\":\"", "").replace("\"}", "");
        } catch (org.springframework.web.client.HttpClientErrorException.Unauthorized ex) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        } catch (org.springframework.web.client.HttpClientErrorException | org.springframework.web.client.ResourceAccessException ex) {
            throw new BadCredentialsException("Error de comunicación: " + ex.getMessage());
        } catch (org.springframework.web.client.RestClientException ex) {
            throw new BadCredentialsException("Error al autenticar: " + ex.getMessage());
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