package com.artxp.artxp.domain.entities;

import com.artxp.artxp.util.Sizes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name="obra")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data

public class ObraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull @Column(name="nombre")
    private String nombre;

    @NonNull @Column(name="fecha_creacion")
    private LocalDate fechaCreacion;

    @NonNull @Column(name="descripcion")
    private String descripcion;

    @NonNull @Column(name="precio_renta")
    private Double precioRenta;

    @NonNull @Column(name="disponibilidad")
    private Boolean disponibilidad;

    //Enum de tamaños
    @NonNull @Column(name="tamanio")
    private Sizes tamano;

    // ----------- KEYS -----------
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tecnica_id", nullable = false)
    @NonNull private TecnicaObraEntity tecnicaObra;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "artista_id", nullable = false)
    @NonNull private MovimientoArtisticoEntity movimientoArtistico;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "movimiento_artistico_id", nullable = false)
    @NonNull private ArtistaEntity artista;

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenEntity> imagenes = new ArrayList<>(); //si permitimos guardar obras sin imagenes para no obtener un null

}
