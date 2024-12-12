package com.artxp.artxp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "imagen")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImagenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@NonNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    //@NonNull
    @Column(name = "url", nullable = false)
    private String url;

    //@NonNull
    @Column(name = "imagen_id", nullable = false)
    private String imagenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "obra_id", nullable = true)
    private ObraEntity obra;

    public ImagenEntity(Integer id, String nombre, String url, String imagenId) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.imagenId = imagenId;
    }

    public ImagenEntity(String nombre, String url, String imagenId) {
        this.nombre = nombre;
        this.url = url;
        this.imagenId = imagenId;
    }
}
