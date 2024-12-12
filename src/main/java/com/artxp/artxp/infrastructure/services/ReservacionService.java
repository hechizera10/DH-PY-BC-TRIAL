package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.ReservacionEntity;
import com.artxp.artxp.domain.entities.TecnicaObraEntity;
import com.artxp.artxp.domain.entities.UsuarioEntity;
import com.artxp.artxp.domain.repositories.ObraRepository;
import com.artxp.artxp.domain.repositories.ReservacionRepository;
import com.artxp.artxp.domain.repositories.UsuarioRepository;
import com.artxp.artxp.util.exeptions.ConflictException;
import com.artxp.artxp.util.exeptions.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservacionService {
    @Autowired
    public ObraRepository obraRepository;
    @Autowired
    public UsuarioService usuarioService;
    @Autowired
    public UsuarioRepository usuarioRepository;

    private final ReservacionRepository reservacionRepository;

    public List<ReservacionEntity> obtenerReservacionesPorRango(Integer obraId, LocalDate fechaInicio, LocalDate fechaFin) {
        return reservacionRepository.findReservacionesByObraAndDateRange(obraId, fechaInicio, fechaFin);
    }

    public List<ObraEntity> obtenerObrasDiponiblesPorRango(LocalDate fechaInicio, LocalDate fechaFin){
        return  reservacionRepository.findObrasDisponibles(fechaInicio,fechaFin);
    }

    // Crear nueva reserva
    public ReservacionEntity crearReservaNueva(Integer obraId, LocalDate fechaInicio, LocalDate fechaFin) {
        String usuarioEmail = usuarioService.obtenerUsuarioAutenticado();

        UsuarioEntity usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<ObraEntity> obraaReservar = Optional.ofNullable(obraRepository.findById(obraId).orElseThrow(() -> new IdNotFoundException(obraId, "Obra")));

        if(!obraaReservar.get().getDisponibilidad()){
            throw new ConflictException(" no se pudo reservar la obra porque  no se encuentra disponible");
        }

        List<ReservacionEntity> reservasenIntervalo = this.obtenerReservacionesPorRango(obraId,fechaInicio,fechaFin);
        if(!reservasenIntervalo.isEmpty()){
          throw new ConflictException(" no se pudo reservaar la obra porque ya tiene una reserva activa en el intervalo indicado");
        }

        ReservacionEntity newReservation = ReservacionEntity.builder()
                .obra(obraaReservar.get())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .usuario(usuario)
                .build();

        // Agregar reservacion al usuario correspondiente
        usuario.getReservaciones().add(newReservation);
        //usuarioService.actualizarUsuario(usuario);

        return reservacionRepository.save(newReservation);

    }

    public void eliminarReservaPorId(Integer id) {
        // Buscar la reserva, lanzar excepción si no existe
        ReservacionEntity reservaBuscada = reservacionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Reservacion"));
        //si existe, eliminar la reserva
        reservacionRepository.deleteById(id);
    }

    // Buscar Reservacion por ID
    public ReservacionEntity findById(Integer id) {
        System.out.println("El id de la reserva es: " + id);
        return reservacionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, "Reservacion"));
    }

    public List<ReservacionEntity> obtenerTodasReservaciones() {
        return reservacionRepository.findAll();
    }

    public ReservacionEntity buscarPorId(Integer id) {
        return reservacionRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException(id, "Reservacion"));
    }

    public ReservacionEntity actualizarReserva(Integer reservaOriginalId, Integer obraId, LocalDate fechaInicio, LocalDate fechaFin) {

        ReservacionEntity reservaAnterior = buscarPorId(reservaOriginalId);
        Optional<ObraEntity> obraaReservar = Optional.ofNullable(obraRepository.findById(obraId).orElseThrow(() -> new IdNotFoundException(obraId, "Obra")));

        if(!obraaReservar.get().getDisponibilidad()){
            throw new ConflictException(" no se pudo reservar la obra porque  no se encuentra disponible");
        }

        List<ReservacionEntity> reservasenIntervalo = this.obtenerReservacionesPorRango(obraId,fechaInicio,fechaFin);
        if(!reservasenIntervalo.isEmpty()){
            throw new ConflictException(" no se pudo reservaar la obra porque ya tiene una reserva activa en el intervalo indicado");
        }

        reservaAnterior.setObra(obraaReservar.get());
        reservaAnterior.setFechaInicio(fechaInicio);
        reservaAnterior.setFechaFin(fechaFin);

        return reservacionRepository.save(reservaAnterior);
    }
}
