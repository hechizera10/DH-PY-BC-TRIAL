package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.*;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.util.exeptions.BadRequestException;
import com.artxp.artxp.util.exeptions.ConflictException;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObraService {
    @Autowired
    public ObraRepository obraRepository;
    @Autowired
    private ArtistaService artistaService;
    @Autowired
    private MovimientoArtisticoService movimientoArtisticoService;
    @Autowired
    private TecnicaObraService tecnicaObraService;
    @Autowired
    private ImagenService imagenService;
    @Autowired
    private CloudinaryService cloudinaryService;

    //Buscar Obra por ID
    public ObraEntity buscarPorId(Integer id){
        return obraRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException(id, "Obra"));
    }

    // Retorna toda la lista de obras
    public List<ObraEntity> buscarTodasLasObras() {
        return obraRepository.findAll();
        // Se convierten las entities a DTOs
        // return obras.stream().map(mapper::obraEntityToDTO).collect(Collectors.toList());
    }

    // Guardar Obra
    public ObraEntity guardarObraNueva(ObraEntity obra, MultipartFile[] files) {

        // Verificar si la obra ya existe por nombre
        Optional<ObraEntity> obraEntityOptional = Optional.empty();
        if (obra.getNombre() != null) {
            obraEntityOptional = obraRepository.findByNombre(obra.getNombre());
        }

        if (obraEntityOptional.isPresent()) {
            throw new ConflictException("La obra ya existe.");
        } else {
            // Manejo de Artista
            ArtistaEntity artistaEntity = artistaService.buscarOCrearArtista(obra.getArtista());

            // Manejo de MovimientoArtistico
            MovimientoArtisticoEntity movimientoArtisticoEntity =
                    movimientoArtisticoService.findById(obra.getMovimientoArtistico().getId());

            // Manejo de TecnicaObra
            TecnicaObraEntity tecnicaObraEntity = tecnicaObraService.buscarOCrearTecnicaObra(obra.getTecnicaObra());

            // Crear ObraEntityToSave
            ObraEntity obraEntityToSave = new ObraEntity();
            obraEntityToSave.setNombre(obra.getNombre());
            obraEntityToSave.setFechaCreacion(obra.getFechaCreacion());
            obraEntityToSave.setDescripcion(obra.getDescripcion());
            obraEntityToSave.setPrecioRenta(obra.getPrecioRenta());
            obraEntityToSave.setDisponibilidad(obra.getDisponibilidad());
            obraEntityToSave.setTamano(obra.getTamano());
            obraEntityToSave.setTecnicaObra(tecnicaObraEntity);
            obraEntityToSave.setMovimientoArtistico(movimientoArtisticoEntity);
            obraEntityToSave.setArtista(artistaEntity);
            obraEntityToSave.setImagenes(new ArrayList<>());

            // Guardar la entidad Obra antes de asociar las imágenes
            ObraEntity savedObraEntity = obraRepository.save(obraEntityToSave);

            List<ImagenEntity> imagenEntities = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    Map result = cloudinaryService.upload(file);
                    ImagenEntity imagen = new ImagenEntity(
                            (String) result.get("original_filename"),
                            (String) result.get("url"),
                            (String) result.get("public_id")
                    );
                    imagen.setObra(savedObraEntity); // Asociar a la entidad Obra guardada
                    imagenEntities.add(imagen);
                } catch (IOException e) {
                    throw new ConflictException("Error al subir las imágenes." + e.getMessage());
                }
            }

            // Actualizar la referencia de la colección de imágenes
            savedObraEntity.getImagenes().addAll(imagenEntities);
            return obraRepository.save(savedObraEntity);

        }
    }

    // Eliminar Obra por id
    public void eliminaObraPorID(Integer idEliminar) {
        Optional<ObraEntity> obraBuscada = Optional.ofNullable(buscarPorId(idEliminar));
        if (obraBuscada.isPresent()) {
            obraRepository.delete(obraBuscada.get());
        } else {
            throw new BadRequestException();
        }
    }

    // Actualizar Obra
    public ObraEntity actualizarObraNueva(ObraEntity obraActualizada, Map<String, MultipartFile> files) throws IOException {

        // Verificar que la obra existe
        ObraEntity obraExistente = buscarPorId(obraActualizada.getId());

        // Actualizar campos de la obra
        ObraEntity obraModificada = new ObraEntity();
        obraModificada.setId(obraExistente.getId());
        obraModificada.setNombre(obraActualizada.getNombre());
        obraModificada.setFechaCreacion(obraActualizada.getFechaCreacion());
        obraModificada.setDescripcion(obraActualizada.getDescripcion());
        obraModificada.setPrecioRenta(obraActualizada.getPrecioRenta());
        obraModificada.setDisponibilidad(obraActualizada.getDisponibilidad());
        obraModificada.setTamano(obraActualizada.getTamano());

        // Actualizar técnica, artista, y movimiento artístico si es necesario
        if (!obraActualizada.getTecnicaObra().getNombre().isEmpty()) {
            obraModificada.setTecnicaObra(tecnicaObraService.buscarOCrearTecnicaObra(obraActualizada.getTecnicaObra()));
        } else {obraModificada.setTecnicaObra(obraExistente.getTecnicaObra());}

        if (!obraActualizada.getArtista().getNombre().isEmpty()) {
            obraModificada.setArtista(artistaService.buscarOCrearArtista(obraActualizada.getArtista()));
        } else {obraModificada.setArtista(obraExistente.getArtista());}

        Integer movimientoNuevoId = obraActualizada.getMovimientoArtistico().getId();
        if (movimientoNuevoId != null) {
            MovimientoArtisticoEntity movimientoBuscado =
                    movimientoArtisticoService.findById(movimientoNuevoId);

            obraModificada.setMovimientoArtistico(movimientoBuscado);
        } else {obraModificada.setMovimientoArtistico(obraExistente.getMovimientoArtistico());}

        obraModificada.setImagenes(new ArrayList<>());

        // Lista para almacenar los IDs de las imágenes que deben mantenerse en la obra
        List<String> imagenesEstaticas = files.entrySet().stream()
                .filter(entry -> entry.getKey().contains("artxp_")) // Filtra solo los campos que contienen "artxp_" en el nombre
                .map(entry -> entry.getKey().split("_")[1]) // Extrae el ID del nombre del campo (e.g., "artxp_123" -> 123)
                .collect(Collectors.toList());

        // Eliminar las imágenes en Cloudinary que ya no están en la lista de IDs enviados
        List<ImagenEntity> imagenesAEliminar = obraActualizada.getImagenes().stream()
                .filter(img -> !imagenesEstaticas.contains(img.getId())) // Si el ID no está en la lista, marcar para eliminar
                .collect(Collectors.toList());

        // Elimina cada imagen de Cloudinary y de la lista de imágenes de la obra
        for (ImagenEntity image : imagenesAEliminar) {
            cloudinaryService.delete(image.getImagenId())
                    .orElseThrow(()-> new IdNotFoundException(Long.parseLong(image.getImagenId()), "Imagen"));
            ImagenEntity imageAEliminar = imagenService.getByCloudId(String.valueOf(image.getImagenId())).get();
            imagenService.delete(imageAEliminar.getId());
        }

        List<ImagenEntity> imagenEntities = new ArrayList<>();

        // Procesar cada archivo en la solicitud
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            MultipartFile file = entry.getValue();
            String fieldName = entry.getKey();

            // Extraer el ID del campo "artxp_" si existe; si no, el archivo es nuevo
            String imageId = fieldName.contains("artxp_") ? fieldName.split("_")[1] : null;



            if (imageId == null) {
                // Imagen nueva: Subir a Cloudinary y crear una nueva entrada en la base de datos
                Map<String, Object> result = cloudinaryService.upload(file);
                ImagenEntity newImage = new ImagenEntity(
                        (String) result.get("original_filename"),
                        (String) result.get("url"),
                        (String) result.get("public_id")
                );
                newImage.setObra(obraModificada); // Asociar la imagen a la obra
                imagenEntities.add(newImage);
            } else {
//                System.out.println("--------->  "+fieldName);
//                System.out.println("----------------++++++++++++++++++   "+imageId);
                imagenEntities.add(imagenService.getByCloudId("artxp_"+imageId).get());
            }
        }

        // Actualizar la referencia de la colección de imágenes
        obraModificada.getImagenes().addAll(imagenEntities);
        return obraRepository.save(obraModificada);
    }
}