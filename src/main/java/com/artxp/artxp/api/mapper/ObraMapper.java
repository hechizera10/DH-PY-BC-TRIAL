//package com.artxp.artxp.api.mapper;
//
//import com.artxp.artxp.api.models.response.*;
//import com.artxp.artxp.domain.entities.*;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper
//public interface ObraMapper {
//
//    ObraMapper INSTANCE = Mappers.getMapper(ObraMapper.class);
//
//    // Mapeo de Obra
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
//    @Mapping(source = "descripcion", target = "descripcion")
//    @Mapping(source = "precioRenta", target = "precioRenta")
//    @Mapping(source = "imagenes", target = "imagenes")
//    @Mapping(source = "disponibilidad", target = "disponibilidad")
//    @Mapping(source = "tamano", target = "tamano")
//    @Mapping(source = "tecnicaObra", target = "tecnicaObra")
//    @Mapping(source = "movimientoArtistico", target = "movimientoArtistico")
//    @Mapping(source = "artista", target = "artista")
//
//    ObraDTO obraEntityToDTO(ObraEntity obraEntity);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
//    @Mapping(source = "descripcion", target = "descripcion")
//    @Mapping(source = "precioRenta", target = "precioRenta")
//    @Mapping(source = "imagenes", target = "imagenes")
//    @Mapping(source = "disponibilidad", target = "disponibilidad")
//    @Mapping(source = "tamano", target = "tamano")
//    @Mapping(source = "tecnicaObra", target = "tecnicaObra")
//    @Mapping(source = "movimientoArtistico", target = "movimientoArtistico")
//    @Mapping(source = "artista", target = "artista")
//    ObraEntity obraDTOToEntity(ObraDTO obraDTO);
//
//    // Mapeo de Artista
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    ArtistaDTO artistaEntityToDTO(ArtistaEntity artistaEntity);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    ArtistaEntity artistaDTOToEntity(ArtistaDTO artistaDTO);
//
//    // Mapeo de MovimientoArtistico
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    MovimientoArtisticoDTO movimientoArtisticoEntityToDTO(MovimientoArtisticoEntity movimientoArtisticoEntity);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    MovimientoArtisticoEntity movimientoArtisticoDTOToEntity(MovimientoArtisticoDTO movimientoArtisticoDTO);
//
//    // Mapeo de TecnicaObra
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    TecnicaObraDTO tecnicaObraEntityToDTO(TecnicaObraEntity tecnicaObraEntity);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    TecnicaObraEntity tecnicaObraDTOToEntity(TecnicaObraDTO tecnicaObraDTO);
//
//    // Mapeo de Imagen
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "url", target = "url")
//    ImagenDTO imagenEntityToDTO(ImagenEntity imagenEntity);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "url", target = "url")
//    ImagenEntity imagenDTOToEntity(ImagenDTO imagenDTO);
//}
