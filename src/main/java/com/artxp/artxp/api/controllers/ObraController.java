package com.artxp.artxp.api.controllers;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.infrastructure.services.*;
import com.artxp.artxp.util.exeptions.ConflictException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/obra")
public class ObraController {
    //----------------------- Dependencias -----------------------
    @Autowired
    private ObraService obraService;

    @Autowired
    private TecnicaObraService tecnicaObraService;

    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private MovimientoArtisticoService movimientoArtisticoService;

    @Autowired
    private CloudinaryService cloudinaryService;

    //----------------------- CRUD -----------------------
    // buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<ObraEntity> buscarObraPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(obraService.buscarPorId(id));
    }

    // lista de obras
    @GetMapping("/listartodos")
    public ResponseEntity<List<ObraEntity>> listarTodo() {
        return ResponseEntity.ok(obraService.buscarTodasLasObras());
    }

    //Agregar nueva Obra
    @PostMapping
    public ResponseEntity<?> guardarObra(@ModelAttribute ObraEntity obra,
                                               @RequestPart("files") MultipartFile[] files) {
        ObraEntity obraEntitySaved = obraService.guardarObraNueva(obra, files);
        return ResponseEntity.ok(obraEntitySaved);
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarObraPorId(@PathVariable("id") Integer idEliminar) {
        obraService.eliminaObraPorID(idEliminar);
        return ResponseEntity.ok("Obra de Arte Eliminada");
    }

    // Editar por id
    @PutMapping
    public ResponseEntity<?> actualizarObra(@ModelAttribute ObraEntity obra,
                                            HttpServletRequest request){

        if (!(request instanceof MultipartHttpServletRequest)) {
            return new ResponseEntity<>("La solicitud debe ser multipart/form-data", HttpStatus.BAD_REQUEST);
        }

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // Obtener el Map de archivos
        Map<String, MultipartFile> files = multiRequest.getFileMap();

        // Llamar al servicio de actualización con la obra y el mapa de archivos
        try {
            return ResponseEntity.ok(obraService.actualizarObraNueva(obra, files));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //paginar
}
