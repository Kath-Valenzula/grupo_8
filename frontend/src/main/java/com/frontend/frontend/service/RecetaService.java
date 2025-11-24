package com.frontend.frontend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.frontend.frontend.config.BackendProperties;
import com.frontend.frontend.model.RecetaDTO;

@Service
public class RecetaService {

    private final RestTemplate restTemplate;
    private final BackendProperties backendProperties;

    public RecetaService(BackendProperties backendProperties) {
        this.restTemplate = new RestTemplate();
        this.backendProperties = backendProperties;
    }

    public List<RecetaDTO> buscarRecetas(String nombre, String tipoCocina, String ingredientes, String paisOrigen, String dificultad, Boolean popular) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(backendProperties.getBaseUrl() + "/api/recetas/buscar");

        if (nombre != null) {
            builder.queryParam("nombre", nombre);
        }
        if (tipoCocina != null) {
            builder.queryParam("tipoCocina", tipoCocina);
        }
        if (ingredientes != null) {
            builder.queryParam("ingredientes", ingredientes);
        }
        if (paisOrigen != null) {
            builder.queryParam("paisOrigen", paisOrigen);
        }
        if (dificultad != null) {
            builder.queryParam("dificultad", dificultad);
        }
        if (popular != null) {
            builder.queryParam("popular", popular);
        }

        String uri = builder.toUriString();
        RecetaDTO[] recetas = restTemplate.getForObject(uri, RecetaDTO[].class);

        return Arrays.asList(recetas);
    }

    public RecetaDTO findById(Long id) {
        String uri = backendProperties.getBaseUrl() + "/api/recetas/" + id;
        return restTemplate.getForObject(uri, RecetaDTO.class);
    }
}
