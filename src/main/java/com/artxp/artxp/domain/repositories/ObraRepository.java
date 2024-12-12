package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.ArtistaEntity;
import com.artxp.artxp.domain.entities.MovimientoArtisticoEntity;
import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.TecnicaObraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ObraRepository extends JpaRepository<ObraEntity,Integer> {
    Optional<ObraEntity> findByNombre(String nombre);
    Optional<List<ObraEntity>> findByTecnicaObra(TecnicaObraEntity tecnicaObraEntity);
    Optional<List<ObraEntity>> findByMovimientoArtistico(MovimientoArtisticoEntity movimientoArtisticoEntity);
    Optional<List<ObraEntity>> findByArtista(ArtistaEntity artistaEntity);
}
