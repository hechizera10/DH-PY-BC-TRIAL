package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.UsuarioEntity;
import com.artxp.artxp.domain.entities.ReservacionEntity;
import com.artxp.artxp.domain.entities.ObraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query("""
            SELECT o.nombre, r.fechaInicio, r.fechaFin
            FROM reservacion r
            INNER JOIN obra o ON r.obra.id = o.id
            WHERE r.usuario.id = :usuarioId
            """)
    List<Object[]> findReservacionesByUsuario(
            @Param("usuarioId") Integer usuarioId);

    Optional<UsuarioEntity> findByEmail(String email);
}
