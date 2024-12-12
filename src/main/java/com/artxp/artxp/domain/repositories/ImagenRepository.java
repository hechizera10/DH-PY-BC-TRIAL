package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.ImagenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagenRepository extends JpaRepository<ImagenEntity, Integer> {
    //List<ImagenEntity> findByObraId(Integer obraId);
    List<ImagenEntity> findByOrderById();
    Optional<ImagenEntity> findFirstByImagenId(String imagenId);
}
