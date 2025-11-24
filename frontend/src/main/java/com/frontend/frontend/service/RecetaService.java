package com.frontend.frontend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.frontend.frontend.TokenStore;
import com.frontend.frontend.model.RecetaDTO;

@Service
public class RecetaService {

    private final RestTemplate restTemplate;
    private final TokenStore tokenStore;

    @Value("${backend.api.base-url:http://localhost:8080}")
    private String backendBaseUrl;

    public RecetaService(TokenStore tokenStore) {
        this.restTemplate = new RestTemplate();
        this.tokenStore = tokenStore;
    }

    @SuppressWarnings({ "null", "deprecation" })
    public List<RecetaDTO> buscarRecetas(String nombre, String tipoCocina, String ingredientes, String paisOrigen, String dificultad, Boolean popular) {

        String uri = UriComponentsBuilder.fromHttpUrl(backendBaseUrl + "/api/recetas/buscar")
                .queryParamIfPresent("nombre", Optional.ofNullable(nombre))
                .queryParamIfPresent("tipoCocina", Optional.ofNullable(tipoCocina))
                .queryParamIfPresent("ingredientes", Optional.ofNullable(ingredientes))
                .queryParamIfPresent("paisOrigen", Optional.ofNullable(paisOrigen))
                .queryParamIfPresent("dificultad", Optional.ofNullable(dificultad))
                .queryParamIfPresent("popular", Optional.ofNullable(popular))
                .toUriString();
        RecetaDTO[] recetas = restTemplate.getForObject(uri, RecetaDTO[].class);

        return Arrays.asList(recetas);
    }

    @SuppressWarnings("null")
    public RecetaDTO findById(Long id) {
        // El endpoint es público en el backend, no requiere token
        String uri = backendBaseUrl + "/api/recetas/" + id;
        return restTemplate.getForObject(uri, RecetaDTO.class);
    }
}
