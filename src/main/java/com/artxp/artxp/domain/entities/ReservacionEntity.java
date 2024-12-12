package com.artxp.artxp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity(name="reservacion")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class ReservacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // Carga Lazy para optimizar consultas
    @JoinColumn(name = "obra_id", nullable = false)
    private ObraEntity obra;

    @NonNull
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @NonNull
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name= "usuaio_id", nullable = false)
    private UsuarioEntity usuario;
}
