package com.artxp.artxp.domain.repositories;


import com.artxp.artxp.domain.entities.TecnicaObraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TecnicaObraRepository extends JpaRepository<TecnicaObraEntity,Integer> {
    List<TecnicaObraEntity> findByNombre(String nombre);
}
