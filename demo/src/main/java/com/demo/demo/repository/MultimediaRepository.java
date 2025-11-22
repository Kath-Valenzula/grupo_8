package com.demo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.demo.models.Multimedia;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
    
    List<Multimedia> findByRecetaId(Long recetaId);
    
    List<Multimedia> findByRecetaIdAndTipo(Long recetaId, String tipo);
}
