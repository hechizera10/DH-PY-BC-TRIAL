package com.artxp.artxp.domain.repositories;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.ReservacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<ReservacionEntity,Integer> {
    //------------------ US H23  reservas por obra especifica ------------------
    // Reservaciones por obra en un rango de fechas
   @Query("SELECT r FROM reservacion r WHERE r.obra.id = :obraId AND "
            + "((r.fechaInicio BETWEEN :fechaInicio AND :fechaFin) OR "
            + "(r.fechaFin BETWEEN :fechaInicio AND :fechaFin) OR "
            + "(r.fechaInicio <= :fechaInicio AND r.fechaFin >= :fechaFin))")
   List<ReservacionEntity> findReservacionesByObraAndDateRange(
            @Param("obraId") Integer obraId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    //------------------ US H22  obras disponibles en rango especifico ------------------
    @Query("""
    SELECT o FROM obra o 
    WHERE o.disponibilidad = TRUE
    AND o.id NOT IN (
        SELECT r.obra.id FROM reservacion r 
        WHERE (r.fechaInicio BETWEEN :fechaInicio AND :fechaFin) 
        OR (r.fechaFin BETWEEN :fechaInicio AND :fechaFin) 
        OR (r.fechaInicio <= :fechaInicio AND r.fechaFin >= :fechaFin)
    )
""")
    List<ObraEntity> findObrasDisponibles(@Param("fechaInicio") LocalDate fechaInicio,
                                          @Param("fechaFin") LocalDate fechaFin);


}
