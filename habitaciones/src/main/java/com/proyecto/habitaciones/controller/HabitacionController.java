package com.proyecto.habitaciones.controller;

import com.proyecto.common.controller.CommonController;
import com.proyecto.common.dto.habitacion.HabitacionRequest;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.habitaciones.service.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
    }

    /**
     * Cambio de estado MANUAL (uso administrativo, vía Gateway).
     * Aplica la restricción: no se puede volver a DISPONIBLE si está OCUPADA.
     */
    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacion(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        service.actualizarEstadoHabitacion(id, idEstado);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambio de estado interno, exclusivo para la comunicación Feign desde
     * msv-reservas (creación, check-in, check-out y cancelación de reservas).
     * No aplica la restricción de la transición manual.
     */
    @PatchMapping("/{id}/estado-sistema/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacionSistema(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        service.actualizarEstadoHabitacionSistema(id, idEstado);
        return ResponseEntity.noContent().build();
    }

}
