package com.artxp.artxp.util.initvalues;
/*
import com.artxp.artxp.api.models.response.ArtistaDTO;
import com.artxp.artxp.api.models.response.MovimientoArtisticoDTO;
import com.artxp.artxp.api.models.response.ObraDTO;
import com.artxp.artxp.api.models.response.TecnicaObraDTO;
import com.artxp.artxp.infrastructure.services.ObraService;
import com.artxp.artxp.util.Sizes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RegistrosDefault implements ApplicationRunner {
    //  -----------  Inyeccion de servicios -----------
    @Autowired
    ObraService obraService;

    //  -----------  Insercion de datos de Ejemplo -----------

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Artista
        ArtistaDTO artista1 = ArtistaDTO.builder()
                .nombre("artistaEjemplo1")
                .build();

        ArtistaDTO artista2 = ArtistaDTO.builder()
                .nombre("artistaEjemplo2")
                .build();

        ArtistaDTO artista3 = ArtistaDTO.builder()
                .nombre("artistaEjemplo3")
                .build();

        // Movimiento
        MovimientoArtisticoDTO mov1 = MovimientoArtisticoDTO.builder()
                .nombreMovimiento("Renacentista")
                .build();

        MovimientoArtisticoDTO mov2 = MovimientoArtisticoDTO.builder()
                .nombreMovimiento("Barroco")
                .build();

        MovimientoArtisticoDTO mov3 = MovimientoArtisticoDTO.builder()
                .nombreMovimiento("Gotico")
                .build();

        // Tecnica
        TecnicaObraDTO tecnica1 = TecnicaObraDTO.builder()
                .nombre("Dibujo en carboncillo")
                .build();

        TecnicaObraDTO tecnica2 = TecnicaObraDTO.builder()
                .nombre("Pintura al oleo")
                .build();

        TecnicaObraDTO tecnica3 = TecnicaObraDTO.builder()
                .nombre("Aquarela")
                .build();

        // Obra
        ObraDTO obra1 = ObraDTO.builder()
                .nombre("Cielo Estrellado")
                .fechaCreacion(LocalDate.of(1872, 7, 13))
                .descripcion("es una obra muy bonita :D")
                .precioRenta(123456d)
                .img("/images/img1.jpg")
                .disponibilidad(true)
                .tecnicaObra(tecnica1)
                .artista(artista1)
                .movimientoArtistico(mov1)
                .tamano(Sizes.PEQUEÑO)
                .build();

        obraService.guardarObraNueva(obra1);

        ObraDTO obra2 = ObraDTO.builder()
                .nombre("Mona Lissa")
                .fechaCreacion(LocalDate.of(1857, 8, 30))
                .descripcion("esto es arte, aprecienloo!!!")
                .precioRenta(999999999d)
                .img("/images/img2.jpg")
                .disponibilidad(false)
                .tecnicaObra(tecnica2)
                .artista(artista2)
                .movimientoArtistico(mov2)
                .tamano(Sizes.MEDIANO)
                .build();

        obraService.guardarObraNueva(obra2);

        ObraDTO obra3 = ObraDTO.builder()
                .nombre("el Grito D=")
                .fechaCreacion(LocalDate.of(2000, 7, 13))
                .descripcion("aaaaaaaaaaaaa")
                .precioRenta(3000d)
                .img("/images/img3.jpg")
                .disponibilidad(true)
                .tecnicaObra(tecnica3)
                .artista(artista3)
                .movimientoArtistico(mov3)
                .tamano(Sizes.GRANDE)
                .build();

        obraService.guardarObraNueva(obra3);
    }

}
*/