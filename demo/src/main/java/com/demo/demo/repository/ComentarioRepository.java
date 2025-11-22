package com.demo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.demo.models.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    List<Comentario> findByRecetaIdOrderByFechaDesc(Long recetaId);
    
    List<Comentario> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
