package com.demo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.demo.models.Valoracion;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    
    List<Valoracion> findByRecetaId(Long recetaId);
    
    Optional<Valoracion> findByRecetaIdAndUsuarioId(Long recetaId, Long usuarioId);
    
    @Query("SELECT AVG(v.rating) FROM Valoracion v WHERE v.receta.id = :recetaId")
    Double calcularPromedioValoracion(Long recetaId);
}
