package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.*;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.domain.repositories.UsuarioRepository;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import com.artxp.artxp.util.exeptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObraRepository obraRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Buscar todos
    public List<UsuarioEntity> buscarTodosUsuarios(){
        return usuarioRepository.findAll();
    }

    //Buscar usuario por ID
    public  UsuarioEntity buscarPorId(Integer id){
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException(id, "Usuario"));
    }

    //Eliminar usuario por ID
    public void eliminaUsuarioPorID(Integer idEliminar) {
        Optional<UsuarioEntity> usuarioEliminar = Optional.ofNullable(buscarPorId(idEliminar));
        if (usuarioEliminar.isPresent()  && usuarioEliminar.get().getRol() != Role.ADMIN) {
            usuarioRepository.delete(usuarioEliminar.get());
        } else {
            throw new UnauthorizedException("Eliminar");
        }
    }

    //Actualizar usuario
    public UsuarioEntity actualizarUsuario(UsuarioEntity usuarioActualizar) {

        // Buscamos el email del usuario autenticado (quien va a actualizar) en el context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioAutenticado = authentication.getName(); // Este es el email del usuario autenticado
        System.out.println(emailUsuarioAutenticado);

        UsuarioEntity usuarioBuscado = usuarioRepository.findById(usuarioActualizar.getId())
                .orElseThrow(() -> new IdNotFoundException(usuarioActualizar.getId(), "Usuario"));

        UsuarioEntity usuarioAutenticado = usuarioRepository.findByEmail(emailUsuarioAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        boolean esAdmin = usuarioAutenticado.getRol() == Role.ADMIN;
        boolean esColab = usuarioAutenticado.getRol() == Role.COLAB;

        usuarioBuscado.setName(usuarioActualizar.getName());
        usuarioBuscado.setLastname(usuarioActualizar.getLastname());
        usuarioBuscado.setEmail(usuarioActualizar.getEmail());
        if (!usuarioActualizar.getPassword().equals(usuarioBuscado.getPassword())) {
            usuarioBuscado.setPassword(passwordEncoder.encode(usuarioActualizar.getPassword()));
        }
        // Permitir que solo el admin cambie el rol (para que Colab no vaya cambiarse como Admin y elimine al Admin original)
        if(usuarioActualizar.getRol() != Role.ADMIN) {
            if (esAdmin || esColab) {
                if (usuarioBuscado.getRol() != Role.ADMIN) {
                    usuarioBuscado.setRol(usuarioActualizar.getRol());
                }
            }
        }else{
            throw new UnauthorizedException("Actualizar");
        }
        return usuarioRepository.save(usuarioBuscado);
    }

    public void agregarFavorito(Integer obraId) {
        // Obtener el usuario autenticado
        String email = obtenerUsuarioAutenticado();

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ObraEntity obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        usuario.getObrasFavoritas().add(obra);
        usuarioRepository.save(usuario);
    }

    public void eliminarFavorito(Integer obraId) {
        String email = obtenerUsuarioAutenticado();

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ObraEntity obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        usuario.getObrasFavoritas().remove(obra);
        usuarioRepository.save(usuario);
    }

    public List<ObraEntity> obtenerObrasFavoritas() {
        String email = obtenerUsuarioAutenticado();

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return usuario.getObrasFavoritas();
    }

    public List<Map<String, Object>> obtenerReservacionesByUsuario() {
        String email = obtenerUsuarioAutenticado();

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Object[]> resultados = usuarioRepository.findReservacionesByUsuario(usuario.getId());
        if (resultados.isEmpty()) {
            throw new IllegalStateException("El usuario no tiene reservas asociadas.");
        }

        return resultados.stream().map(obj -> {
            Map<String, Object> reserva = new HashMap<>();
            reserva.put("nombreObra", obj[0]);
            reserva.put("fechaInicio", obj[1]);
            reserva.put("fechaFin", obj[2]);
            return reserva;
        }).toList();
    }

    // ---------METODOS AUXILIARES ---------
    public String obtenerUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Devuelve el email o username
        }
        return principal.toString();
    }
}
