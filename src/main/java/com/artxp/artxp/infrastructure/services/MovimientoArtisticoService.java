package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.ImagenEntity;
import com.artxp.artxp.domain.entities.MovimientoArtisticoEntity;
import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.repositories.MovimientoArtisticoRepository;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.util.exeptions.ConflictException;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimientoArtisticoService {
    @Autowired
    private MovimientoArtisticoRepository movRepository;
    @Autowired
    private ObraRepository obraRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Guardar Movimiento Artistico con imagen
    public MovimientoArtisticoEntity guardarMovimientoArtistico(MovimientoArtisticoEntity movimientoArtisticoEntity, MultipartFile file) throws IOException {
        // Verificar si el movimiento artístico ya existe por nombre
        Optional<MovimientoArtisticoEntity> movimientoArtisticoEntityOptional =
                movRepository.findByNombre(movimientoArtisticoEntity.getNombre()).stream().findFirst();

        if (movimientoArtisticoEntityOptional.isPresent()) {
            throw new ConflictException("El Movimiento Artístico ya existe.");
        } else {
            // Crear Movimiento artistico a guardar en BD
            MovimientoArtisticoEntity movArtisticoEntityToSave = new MovimientoArtisticoEntity();
            movArtisticoEntityToSave.setNombre(movimientoArtisticoEntity.getNombre());
            movArtisticoEntityToSave.setDescripcion(movimientoArtisticoEntity.getDescripcion());

            // Subir imagen a Cloudinary
            Map result = cloudinaryService.upload(file);

            // Crear una instancia de ImagenEntity con la URL de la imagen
            ImagenEntity imagen = new ImagenEntity(
                    (String) result.get("original_filename"),
                    (String) result.get("url"),
                    (String) result.get("public_id")
            );

            // Asignar la instancia de ImagenEntity al movimiento artístico
            movArtisticoEntityToSave.setImagen(imagen);

            // Guardar Movimiento Artístico
            return movRepository.save(movArtisticoEntityToSave);
        }
    }

    // Buscar Movimiento Artistico por ID
    public MovimientoArtisticoEntity findById(Integer id) {
        System.out.println("El id del Movimiento Artístico es: " + id);
        return movRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "MovimientoArtistico"));
    }

    // Retorna toda la lista de movimientos artisticos
    public List<MovimientoArtisticoEntity> buscarTodosLosMovArtisticos() {
        List<MovimientoArtisticoEntity> movArtisticos = movRepository.findAll();
        return movArtisticos;
    }

    // Buscar Obra con el Movimiento Arístico buscado
    public List<ObraEntity> obraConMovimiento(Integer movimientoId) throws RuntimeException {
        Optional<MovimientoArtisticoEntity> movimientoArtisticoBuscado = Optional.ofNullable(findById(movimientoId));
        if (movimientoArtisticoBuscado.isPresent()) {
            return obraRepository.findByMovimientoArtistico(movimientoArtisticoBuscado.get()).get();
        } else {
            throw new IdNotFoundException(movimientoId, "Movimiento Artistico");
        }
    }

    //Eliminar Movimiento Artístico
    public void eliminarMovimientoArtisticoPorId(Integer id){
        // Buscar el Movimiento Artistico, lanzar excepción si no existe
        MovimientoArtisticoEntity movimientoArtisticoBuscada = movRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Movimiento Artistico"));

        // Verificar si el Movimiento Artistico está asociada a alguna obra
        List<ObraEntity> obrasAsociadas = obraConMovimiento(id);
        if (!obrasAsociadas.isEmpty()) {
            // Crear un mensaje que incluya los nombres de las obras asociadas
            String mensajeError = "No se puede eliminar el Movimiento Artistico, ya que está asociado a las siguientes obras: " +
                    obrasAsociadas.stream()
                            .map(ObraEntity::getNombre)
                            .collect(Collectors.joining(", "));
            throw new ConflictException(mensajeError);
        }

        // Si no hay obras asociadas, eliminar la técnica
        movRepository.deleteById(id);
    }

    // Actualizar Movimiento Artístico
    public MovimientoArtisticoEntity actualizarMovimientoArtistico(MovimientoArtisticoEntity movimientoArtisticoActualizado, MultipartFile file) throws IOException {
        // Buscar el Movimiento Artisitico, lanzar excepción si no existe
        MovimientoArtisticoEntity movimientoArtisticoBuscado = movRepository.findById(movimientoArtisticoActualizado.getId())
                .orElseThrow(() -> new IdNotFoundException(movimientoArtisticoActualizado.getId(), "Movimiento Artístico"));

        // Subir nueva imagen a Cloudinary si se proporciona un archivo
        if (file != null && !file.isEmpty()) {
            // Eliminar la imagen anterior de Cloudinary
            if (movimientoArtisticoBuscado.getImagen() != null) {
                cloudinaryService.delete(movimientoArtisticoBuscado.getImagen().getImagenId());
            }

            // Subir nueva imagen a Cloudinary
            Map result = cloudinaryService.upload(file);

            // Crear una instancia de ImagenEntity con la URL de la nueva imagen
            ImagenEntity nuevaImagen = new ImagenEntity(
                    (String) result.get("original_filename"),
                    (String) result.get("url"),
                    (String) result.get("public_id")
            );

            // Asignar la nueva instancia de ImagenEntity al movimiento artístico
            movimientoArtisticoActualizado.setImagen(nuevaImagen);
        } else {
            // Mantener la imagen existente si no se proporciona un nuevo archivo
            movimientoArtisticoActualizado.setImagen(movimientoArtisticoBuscado.getImagen());
        }

        // Actualizar los demás campos del movimiento artístico
        movimientoArtisticoBuscado.setNombre(movimientoArtisticoActualizado.getNombre());
        movimientoArtisticoBuscado.setDescripcion(movimientoArtisticoActualizado.getDescripcion());
        movimientoArtisticoBuscado.setImagen(movimientoArtisticoActualizado.getImagen());

        // Guardar Movimiento Artístico actualizado
        return movRepository.save(movimientoArtisticoBuscado);
    }
}
