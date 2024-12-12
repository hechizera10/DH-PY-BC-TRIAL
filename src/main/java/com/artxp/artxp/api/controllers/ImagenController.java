package com.artxp.artxp.api.controllers;

import com.artxp.artxp.domain.entities.ImagenEntity;
import com.artxp.artxp.infrastructure.services.CloudinaryService;
import com.artxp.artxp.infrastructure.services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/imagenes")
@CrossOrigin
public class ImagenController {
    //----------------------- Dependencias -----------------------
    @Autowired
    private ImagenService imagenService;

    @Autowired
    CloudinaryService cloudinaryService;

    @GetMapping("/list")
    public ResponseEntity<List<ImagenEntity>> list(){
        List<ImagenEntity> list = imagenService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile multipartFile)throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return new ResponseEntity("imagen no válida", HttpStatus.BAD_REQUEST);
        }
        Map result = cloudinaryService.upload(multipartFile);
        //System.out.println(result);
        ImagenEntity imagen =
                new ImagenEntity((String)result.get("original_filename"),
                        (String)result.get("url"),
                        (String)result.get("public_id"));
        imagenService.save(imagen);
        return new ResponseEntity("imagen subida", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)throws IOException {
        if(!imagenService.exists(id))
            return new ResponseEntity("no existe", HttpStatus.NOT_FOUND);
        ImagenEntity imagen = imagenService.getOne(id).get();
        Map result = cloudinaryService.delete(imagen.getImagenId()).get();
        imagenService.delete(id);
        return new ResponseEntity("imagen eliminada", HttpStatus.OK);
    }

/*
    @Autowired
    private ObraService obraService;

    //crear nueva imagen
    @PostMapping("/{obra_id}")
    public ResponseEntity<ImagenDTO> crearImagen(@PathVariable Integer obra_id, @RequestBody ImagenDTO imagenDTO) {
        ObraEntity obra = obraService.buscarPorId(obra_id);
        ImagenDTO nuevaImagen = imagenService.guardarImagen(imagenDTO, obra);
        return ResponseEntity.ok(nuevaImagen);
    }

    // obtener imagen por ID de obra
    @GetMapping("imagen/{obra_id}")
    public ResponseEntity<List<ImagenDTO>> buscarImagenesPorObra(@PathVariable Integer obra_id) {
        ObraEntity obra = obraService.buscarPorId(obra_id);
        List<ImagenDTO> imagenes = imagenService.buscarImagenesPorObra(obra);
        return ResponseEntity.ok(imagenes);
    }

    // Eliminar imagen por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagen(@PathVariable Integer id) {
        imagenService.eliminaImagenPorID(id);
        return ResponseEntity.ok("Imagen eliminada con éxito");
    }
*/
}
