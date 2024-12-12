package com.artxp.artxp.api.controllers;

import com.artxp.artxp.domain.entities.ObraEntity;
import com.artxp.artxp.domain.entities.ReservacionEntity;
import com.artxp.artxp.infrastructure.services.ReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/reservas")
public class ReservasController {
    //--------------------- Dependencias ---------------------
    @Autowired
    private ReservacionService reservacionService;

//----------------------- Mapeos -----------------------

// --------- CRUD DE RESERVAS -----------

// Create -->
    @PostMapping // Generar nueva reserva
    public ResponseEntity<ReservacionEntity> crearReserva(@RequestBody Map<String, Object> requestData) {
        // Extraer datos del JSON
        Integer obraId = (Integer) requestData.get("obra");
        LocalDate fechaInicio = LocalDate.parse((String) requestData.get("fechaInicio"));
        LocalDate fechaFin = LocalDate.parse((String) requestData.get("fechaFin"));

        // Crear la nueva reserva usando el servicio
        ReservacionEntity nuevaReserva = reservacionService.crearReservaNueva(obraId, fechaInicio, fechaFin);
        return ResponseEntity.ok(nuevaReserva);
    }


// Read -->
    // Obetener todas las Reservas
    @GetMapping("/listartodas")
    public ResponseEntity<List<ReservacionEntity>> buscarTodasReserva(){
        List<ReservacionEntity> listaReservas = reservacionService.obtenerTodasReservaciones();
        return ResponseEntity.ok(listaReservas);
    }

    // Obtener reserva por Id
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionEntity> buscarReservaPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(reservacionService.buscarPorId(id));
    }


    // Obtener obras disponibles en rango especifico
    @GetMapping("/disponibles")
    public ResponseEntity<List<ObraEntity>> buscarObrasDisponibles(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<ObraEntity> obras = reservacionService.obtenerObrasDiponiblesPorRango(fechaInicio, fechaFin);
        return ResponseEntity.ok(obras);
    }
// Update -->

    @PutMapping // Actualizar reserva Existente
    public ResponseEntity<ReservacionEntity> actualizarReserva(@RequestBody Map<String, Object> requestData) {
        // Extraer datos del JSON
        Integer reservaOriginalId =  (Integer) requestData.get("reservaId");
        Integer obraId = (Integer) requestData.get("obra");
        LocalDate fechaInicio = LocalDate.parse((String) requestData.get("fechaInicio"));
        LocalDate fechaFin = LocalDate.parse((String) requestData.get("fechaFin"));

        // Crear la nueva reserva usando el servicio
        ReservacionEntity nuevaReserva = reservacionService.actualizarReserva(reservaOriginalId, obraId, fechaInicio, fechaFin);
        return ResponseEntity.ok(nuevaReserva);
    }

// Delete -->
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReservaPorId(@PathVariable("id") Integer id) {
        reservacionService.eliminarReservaPorId(id);
        return ResponseEntity.ok("Éxito al eliminar la Reserva");
    }



    // Metodos de Apoyo -->
    // Obtener reservas por obra especifica
    @GetMapping("/{obraId}/rango-no-disponible")
    public ResponseEntity<List<ReservacionEntity>> obtenerFechasNoDisponibles(
            @PathVariable("obraId") Integer obraId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<ReservacionEntity> reservaciones = reservacionService.obtenerReservacionesPorRango(obraId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reservaciones);
    }







}
