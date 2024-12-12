package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.MovimientoArtisticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovimientoArtisticoRepository extends JpaRepository<MovimientoArtisticoEntity,Integer> {
    List<MovimientoArtisticoEntity> findByNombre(String nombre);
}
