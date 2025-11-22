package com.demo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.models.Comentario;
import com.demo.demo.models.Multimedia;
import com.demo.demo.models.Receta;
import com.demo.demo.models.User;
import com.demo.demo.models.Valoracion;
import com.demo.demo.repository.ComentarioRepository;
import com.demo.demo.repository.MultimediaRepository;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.repository.ValoracionRepository;
import com.demo.demo.service.RecetaService;

import jakarta.validation.Valid;

/**
 * API REST para funcionalidades privadas de recetas
 * Todos los endpoints requieren autenticación con JWT
 */
@RestController
@RequestMapping("/api/recetas")
public class RecetaFuncionesPrivadasController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private UserRepository userRepository;

    // ============= COMENTARIOS =============

    /**
     * GET /api/recetas/{id}/comentarios
     * Obtiene todos los comentarios de una receta (público)
     */
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<Comentario>> obtenerComentarios(@PathVariable Long id) {
        List<Comentario> comentarios = comentarioRepository.findByRecetaIdOrderByFechaDesc(id);
        return ResponseEntity.ok(comentarios);
    }

    /**
     * POST /api/recetas/{id}/comentarios
     * Agrega un comentario a una receta (requiere JWT)
     */
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable Long id,
                                                @Valid @RequestBody Comentario comentario,
                                                Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para comentar");
        }

        var recetaOpt = recetaService.obtenerPorId(id);
        if (recetaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Receta no encontrada");
        }
        
        Receta receta = recetaOpt.get();
        String username = authentication.getName();
        User usuario = userRepository.findByUsername(username);
        
        comentario.setReceta(receta);
        comentario.setUsuario(usuario);
        
        Comentario comentarioGuardado = comentarioRepository.save(comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioGuardado);
    }

    // ============= VALORACIONES =============

    /**
     * GET /api/recetas/{id}/valoraciones
     * Obtiene las valoraciones de una receta (público)
     */
    @GetMapping("/{id}/valoraciones")
    public ResponseEntity<ValoracionResponse> obtenerValoraciones(@PathVariable Long id) {
        List<Valoracion> valoraciones = valoracionRepository.findByRecetaId(id);
        Double promedio = valoracionRepository.calcularPromedioValoracion(id);
        
        ValoracionResponse response = new ValoracionResponse(
            valoraciones,
            promedio != null ? promedio : 0.0,
            valoraciones.size()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/recetas/{id}/valoraciones
     * Agrega una valoración a una receta (requiere JWT)
     */
    @PostMapping("/{id}/valoraciones")
    public ResponseEntity<?> valorarReceta(@PathVariable Long id,
                                            @Valid @RequestBody Valoracion valoracion,
                                            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para valorar");
        }

        var recetaOpt = recetaService.obtenerPorId(id);
        if (recetaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Receta no encontrada");
        }
        
        Receta receta = recetaOpt.get();
        String username = authentication.getName();
        User usuario = userRepository.findByUsername(username);
        
        // Verificar si el usuario ya valoró esta receta
        var valoracionExistente = valoracionRepository
                .findByRecetaIdAndUsuarioId(id, Long.valueOf(usuario.getId()));
        
        if (valoracionExistente.isPresent()) {
            // Actualizar valoración existente
            Valoracion v = valoracionExistente.get();
            v.setRating(valoracion.getRating());
            Valoracion valoracionActualizada = valoracionRepository.save(v);
            return ResponseEntity.ok(valoracionActualizada);
        } else {
            // Crear nueva valoración
            valoracion.setReceta(receta);
            valoracion.setUsuario(usuario);
            Valoracion valoracionGuardada = valoracionRepository.save(valoracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(valoracionGuardada);
        }
    }

    // ============= MULTIMEDIA (Fotos/Videos) =============

    /**
     * GET /api/recetas/{id}/multimedia
     * Obtiene todos los archivos multimedia de una receta (público)
     */
    @GetMapping("/{id}/multimedia")
    public ResponseEntity<List<Multimedia>> obtenerMultimedia(@PathVariable Long id) {
        List<Multimedia> archivos = multimediaRepository.findByRecetaId(id);
        return ResponseEntity.ok(archivos);
    }

    /**
     * POST /api/recetas/{id}/multimedia
     * Agrega una foto o video a una receta (requiere JWT)
     */
    @PostMapping("/{id}/multimedia")
    public ResponseEntity<?> agregarMultimedia(@PathVariable Long id,
                                                @Valid @RequestBody Multimedia multimedia,
                                                Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debe autenticarse para subir archivos");
        }

        var recetaOpt = recetaService.obtenerPorId(id);
        if (recetaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Receta no encontrada");
        }
        
        Receta receta = recetaOpt.get();
        multimedia.setReceta(receta);
        Multimedia multimediaGuardado = multimediaRepository.save(multimedia);
        return ResponseEntity.status(HttpStatus.CREATED).body(multimediaGuardado);
    }
    
    /**
     * Clase interna para respuesta de valoraciones
     */
    public static class ValoracionResponse {
        private final List<Valoracion> valoraciones;
        private final Double promedioRating;
        private final Integer totalValoraciones;
        
        public ValoracionResponse(List<Valoracion> valoraciones, Double promedioRating, Integer totalValoraciones) {
            this.valoraciones = valoraciones;
            this.promedioRating = promedioRating;
            this.totalValoraciones = totalValoraciones;
        }
        
        public List<Valoracion> getValoraciones() {
            return valoraciones;
        }
        
        public Double getPromedioRating() {
            return promedioRating;
        }
        
        public Integer getTotalValoraciones() {
            return totalValoraciones;
        }
    }
}
