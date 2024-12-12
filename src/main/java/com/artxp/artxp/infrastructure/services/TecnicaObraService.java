package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.TecnicaObraEntity;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.domain.repositories.TecnicaObraRepository;
import com.artxp.artxp.util.exeptions.ConflictException;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TecnicaObraService {
    @Autowired
    private TecnicaObraRepository tecnicaObraRepository;
    @Autowired
    private ObraRepository obraRepository;

    // Se busca una técnica de obra por el nombre en caso de que exista se retorna, si no existe se crea
    public TecnicaObraEntity buscarOCrearTecnicaObra(TecnicaObraEntity tecnicaObraEntity) {
        System.out.println("Buscando Técnica con nombre: " + tecnicaObraEntity.getNombre());
        Optional<TecnicaObraEntity> tecnicaObraEntityOptional =
                tecnicaObraRepository.findByNombre(tecnicaObraEntity.getNombre()).stream().findFirst();

        TecnicaObraEntity tecnicaObraEntityResult;

        if (tecnicaObraEntityOptional.isPresent()) {
            System.out.println("Técnica encontrado: " + tecnicaObraEntityOptional.get().getId());
//            throw new ConflictException("La Técnica ya existe.");
            tecnicaObraEntityResult = tecnicaObraEntityOptional.get();

        } else {
            System.out.println("Creando nuevo Técnica: " + tecnicaObraEntity.getNombre());

            // Guardar la entidad y hacer flush para asegurar que el ID se genere
            tecnicaObraEntityResult = tecnicaObraRepository.saveAndFlush(tecnicaObraEntity);

            System.out.println("Nuevo Técnica guardado con ID: " + tecnicaObraEntityResult.getId());
        }

        return tecnicaObraEntityResult;
    }

    // Buscar Tecnica obra por ID
    public TecnicaObraEntity findById(Integer id) {
        System.out.println("El id de la Técnica es: " + id);
        return tecnicaObraRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "TecnicaObra"));
    }

    // Retorna toda la lista de tecnicas
    public List<TecnicaObraEntity> buscarTodasLasTecnicas() {
        List<TecnicaObraEntity> tecnicas = tecnicaObraRepository.findAll();
        return tecnicas;
    }

    //Buscar Obras con la Tecnica buscada
    public List<ObraEntity> obrasConTecnica(Integer tecnicaId) throws RuntimeException {
        Optional<TecnicaObraEntity> tecnicaBuscada = Optional.ofNullable(findById(tecnicaId));
        if (tecnicaBuscada.isPresent()) {
            return obraRepository.findByTecnicaObra(tecnicaBuscada.get()).get();
        } else {
            throw new IdNotFoundException(tecnicaId, "Técnica");
        }
    }

    // Eliminar tecnica
    public void eliminarTecnicaPorId(Integer id) {
        // Buscar la técnica, lanzar excepción si no existe
        TecnicaObraEntity tecnicaBuscada = tecnicaObraRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Técnica"));

        // Verificar si la técnica está asociada a alguna obra
        List<ObraEntity> obrasAsociadas = obrasConTecnica(id);
        if (!obrasAsociadas.isEmpty()) {
            // Crear un mensaje que incluya los nombres de las obras asociadas
            String mensajeError = "No se puede eliminar la técnica, ya que está asociada a las siguientes obras: " +
                    obrasAsociadas.stream()
                            .map(ObraEntity::getNombre)
                            .collect(Collectors.joining(", "));
            throw new ConflictException(mensajeError);
        }

        // Si no hay obras asociadas, eliminar la técnica
        tecnicaObraRepository.deleteById(id);
    }

    //Actualizar tecnica
    public TecnicaObraEntity actualizarTecnica(TecnicaObraEntity tecnicaActualizada){
        // Buscar la técnica, lanzar excepción si no existe
        TecnicaObraEntity tecnicaBuscada = tecnicaObraRepository.findById(tecnicaActualizada.getId())
                .orElseThrow(() -> new IdNotFoundException(tecnicaActualizada.getId(), "Técnica"));

        TecnicaObraEntity tecnicaActualizacion = TecnicaObraEntity.builder()
                .id(tecnicaBuscada.getId())
                .nombre(tecnicaActualizada.getNombre())
                .build();
//        System.out.println("___________________>>>>>>>>>>>>>>>>"+tecnicaActualizacion);
        return tecnicaObraRepository.save(tecnicaActualizacion);
    }
}
