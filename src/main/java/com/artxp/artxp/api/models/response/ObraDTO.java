package com.artxp.artxp.api.models.response;

import com.artxp.artxp.util.Sizes;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObraDTO {

    private Integer id;
    private String nombre;
    private LocalDate fechaCreacion;
    private String descripcion;
    private Double precioRenta;
    private Boolean disponibilidad;

    //Enum de tamaños
    private Sizes tamano;

    //Referencia a otro DTOs
    private TecnicaObraDTO tecnicaObra;
    private MovimientoArtisticoDTO movimientoArtistico;
    private ArtistaDTO artista;
    private List<ImagenDTO> imagenes;
}
