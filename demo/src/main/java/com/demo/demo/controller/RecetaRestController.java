package com.demo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.models.Receta;
import com.demo.demo.service.RecetaService;

import jakarta.validation.Valid;

/**
 * API REST para gestión de recetas
 * 
 * Endpoints públicos (sin JWT):
 * - GET /api/recetas - Listar todas las recetas
 * - GET /api/recetas/{id} - Obtener una receta por ID
 * - GET /api/recetas/buscar - Buscar recetas con filtros
 * 
 * Endpoints privados (requieren JWT):
 * - POST /api/recetas - Crear nueva receta
 * - PUT /api/recetas/{id} - Actualizar receta existente
 * - DELETE /api/recetas/{id} - Eliminar receta
 */
@RestController
@RequestMapping("/api/recetas")
public class RecetaRestController {

    @Autowired
    private RecetaService recetaService;

    /**
     * GET /api/recetas
     * Obtiene todas las recetas (público)
     */
    @GetMapping
    public ResponseEntity<List<Receta>> listarRecetas() {
        List<Receta> recetas = recetaService.listarTodas();
        return ResponseEntity.ok(recetas);
    }

    /**
     * GET /api/recetas/{id}
     * Obtiene una receta por ID (público)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReceta(@PathVariable Long id) {
        return recetaService.obtenerPorId(id)
                .map(receta -> ResponseEntity.ok(receta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    /**
     * GET /api/recetas/buscar
     * Busca recetas con filtros (público)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Receta>> buscarRecetas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingredientes,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            @RequestParam(required = false) Boolean popular) {
        
        List<Receta> recetas = recetaService.buscar(nombre, tipoCocina, ingredientes, 
                                                     paisOrigen, dificultad, popular);
        return ResponseEntity.ok(recetas);
    }

    /**
     * POST /api/recetas
     * Crea una nueva receta (requiere JWT)
     */
    @PostMapping
    public ResponseEntity<?> crearReceta(@Valid @RequestBody Receta receta, 
                                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para crear recetas");
        }
        
        Receta recetaGuardada = recetaService.guardar(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaGuardada);
    }

    /**
     * PUT /api/recetas/{id}
     * Actualiza una receta existente (requiere JWT)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReceta(@PathVariable Long id, 
                                               @Valid @RequestBody Receta receta,
                                               Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para actualizar recetas");
        }
        
        return recetaService.obtenerPorId(id)
                .map(recetaExistente -> {
                    receta.setId(id);
                    Receta recetaActualizada = recetaService.guardar(receta);
                    return ResponseEntity.ok(recetaActualizada);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * DELETE /api/recetas/{id}
     * Elimina una receta (requiere JWT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReceta(@PathVariable Long id, 
                                            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para eliminar recetas");
        }
        
        return recetaService.obtenerPorId(id)
                .map(receta -> {
                    recetaService.eliminarReceta(id);
                    return ResponseEntity.ok().body("Receta eliminada exitosamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Receta no encontrada"));
    }
}
