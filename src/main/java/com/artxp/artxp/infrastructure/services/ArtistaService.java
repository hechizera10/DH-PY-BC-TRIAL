package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.ArtistaEntity;
import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.repositories.ArtistaRepository;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.util.exeptions.ConflictException;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistaService {
    @Autowired
    private ArtistaRepository artistaRepository;
    @Autowired
    private ObraRepository obraRepository;

    // Se busca un artista por el nombre en caso de que exista se retorna, si no existe se crea
    public ArtistaEntity buscarOCrearArtista(ArtistaEntity artistaEntity) {
        System.out.println("Buscando artista con nombre: " + artistaEntity.getNombre());
        Optional<ArtistaEntity> artistaEntityOptional =
                artistaRepository.findByNombre(artistaEntity.getNombre()).stream().findFirst();

        ArtistaEntity artistaEntityResult;

        if (artistaEntityOptional.isPresent()) {
            System.out.println("Artista encontrado: " + artistaEntityOptional.get().getId());
//            throw new ConflictException("El Artista ya existe.");
            artistaEntityResult = artistaEntityOptional.get();

        } else {
            System.out.println("Creando nuevo Artista: " + artistaEntity.getNombre());

            // Guardar la entidad y hacer flush para asegurar que el ID se genere
            artistaEntityResult = artistaRepository.saveAndFlush(artistaEntity);

            System.out.println("Nuevo artista guardado con ID: " + artistaEntityResult.getId());
        }

        return artistaEntityResult;
    }

    // Buscar Artista por ID
    public ArtistaEntity findById(Integer id) {
        System.out.println("El id del Artista es: " + id);
        return artistaRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Artista"));
    }

    // Retorna toda la lista de artistas
    public List<ArtistaEntity> buscarTodosLosArtistas() {
        List<ArtistaEntity> artistas = artistaRepository.findAll();
        return artistas;
    }

    // Buscar Obra con el Artista buscado
    public List<ObraEntity> obraConArtista(Integer artistaId) throws RuntimeException {
        Optional<ArtistaEntity> artistaBuscado = Optional.ofNullable(findById(artistaId));
        if (artistaBuscado.isPresent()){
            return obraRepository.findByArtista(artistaBuscado.get()).get();
        }else{
            throw new IdNotFoundException(artistaId, "Artista");
        }
    }

    // Eliminar Artista
    public void eliminarArtistaPorId(Integer id){
        // Buscar el Artista, lanzar excepción si no existe
        ArtistaEntity artistaBuscado = artistaRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Artista"));

        // Verificar si el Artista está asociado a alguna obra
        List<ObraEntity> obrasAsociadas = obraConArtista(id);
        if (!obrasAsociadas.isEmpty()) {
            //Crear un mensaje que incluya los nombres de las obras asociadas
            String mensajeError = "No se puede eliminar el Artista, ya que está asociado a las siguientes obras: " +
                    obrasAsociadas.stream()
                            .map(ObraEntity::getNombre)
                            .collect(Collectors.joining(", "));
            throw new ConflictException(mensajeError);
        }

        // Si no hay obras asociadas, elimnar el artista
        artistaRepository.deleteById(id);
    }

    //Actualizar Artista
    public ArtistaEntity actualizarArtista(ArtistaEntity artistaActualizado) {
        // Buscar el Artista, lanzar excepción si no existe
        ArtistaEntity artistaBuscado = artistaRepository.findById(artistaActualizado.getId())
                .orElseThrow(() -> new IdNotFoundException(artistaActualizado.getId(), "Artista"));

        ArtistaEntity artistaActualizacion = ArtistaEntity.builder()
                .id(artistaBuscado.getId())
                .nombre(artistaActualizado.getNombre())
                .build();
        return artistaRepository.save(artistaActualizado);
    }
}
