package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.ArtistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ArtistaRepository extends JpaRepository<ArtistaEntity,Integer> {
    List<ArtistaEntity> findByNombre(String nombre);
}
