package com.artxp.artxp.api.controllers;

import com.artxp.artxp.domain.entities.MovimientoArtisticoEntity;
import com.artxp.artxp.infrastructure.services.MovimientoArtisticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path="/movimientoArtistico")
public class MovimientoArtisticoController {
    //----------------------- Dependencias -----------------------
    @Autowired
    private MovimientoArtisticoService movimientoArtisticoService;

    //----------------------- Mapeos -----------------------

    // Guardar un movimiento artistico con imagen
    @PostMapping
    public ResponseEntity<MovimientoArtisticoEntity> guardarMovimientoArtisticoConImagen(
            @ModelAttribute MovimientoArtisticoEntity movimientoArtisticoEntity,
            @RequestPart("file") MultipartFile file) throws IOException {
        // Agregar logs para verificar los datos recibidos
        System.out.println("Recibido archivo: " + file.getOriginalFilename());

        return ResponseEntity.ok(movimientoArtisticoService.guardarMovimientoArtistico(movimientoArtisticoEntity, file));
    }

    // lista de movimientos artisticos
    @GetMapping("/listartodos")
    public ResponseEntity<List<MovimientoArtisticoEntity>> listarTodo() {
        return ResponseEntity.ok(movimientoArtisticoService.buscarTodosLosMovArtisticos());
    }

    // Busacar Movimiento Artístico por id
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoArtisticoEntity> buscarMovimientoArtísticoPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(movimientoArtisticoService.findById(id));
    }

    // Actualizar Movimiento Artístico
    @PutMapping
    public ResponseEntity<MovimientoArtisticoEntity> actualizarMovimientoArtistico(
            @ModelAttribute MovimientoArtisticoEntity movimientoArtisticoEntity,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(movimientoArtisticoService.actualizarMovimientoArtistico(movimientoArtisticoEntity, file));
    }

    // Eliminar Movimiento Artístico
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMovimientoArtistico(@PathVariable("id") Integer id) {
        movimientoArtisticoService.eliminarMovimientoArtisticoPorId(id);
        return ResponseEntity.ok("Éxito al eliminar el Movimiento Artístico");
    }
}
