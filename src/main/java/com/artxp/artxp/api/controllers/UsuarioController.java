package com.artxp.artxp.api.controllers;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.ReservacionEntity;
import com.artxp.artxp.domain.entities.UsuarioEntity;
import com.artxp.artxp.infrastructure.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    // Crear usuarios está en el registro de usuario (AuthenticationController)

    // Listar usuarios
    @GetMapping("/listartodos")
    public ResponseEntity<List<UsuarioEntity>> listarTodo() {
        return ResponseEntity.ok(usuarioService.buscarTodosUsuarios());
    }

    // Buscar usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // Actualizar usuario
    @PutMapping
    public ResponseEntity<UsuarioEntity> actualizarUsuario(@RequestBody UsuarioEntity usuarioEntity){
        return ResponseEntity.ok(usuarioService.actualizarUsuario(usuarioEntity));
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("id") Integer id) {
        usuarioService.eliminaUsuarioPorID(id);
        return ResponseEntity.ok("Éxito al eliminar el Usuario");
    }

    // Agregar obra a favoritos
    @PostMapping("/favoritos/{obraId}")
    public ResponseEntity<String> agregarFavorito(@PathVariable Integer obraId) {
        usuarioService.agregarFavorito(obraId);
        return ResponseEntity.ok("Obra agregada a favoritos");
    }

    // Eliminar obra de favoritos
    @DeleteMapping("/favoritos/{obraId}")
    public ResponseEntity<String> eliminarFavorito(@PathVariable Integer obraId) {
        usuarioService.eliminarFavorito(obraId);
        return ResponseEntity.ok("Obra eliminada de favoritos");
    }

    // Obtener todas las obras favoritas del usuario
    @GetMapping("/favoritos")
    public ResponseEntity<List<ObraEntity>> obtenerObrasFavoritas() {
        return ResponseEntity.ok(usuarioService.obtenerObrasFavoritas());
    }

    // Obtener todas las obras favoritas del usuario
    @GetMapping("/reservaciones")
    public ResponseEntity<List<Map<String, Object>>> obtenerReservacionesByUsuario() {
        List<Map<String, Object>> reservas = usuarioService.obtenerReservacionesByUsuario();
        return ResponseEntity.ok(reservas);
    }
}
