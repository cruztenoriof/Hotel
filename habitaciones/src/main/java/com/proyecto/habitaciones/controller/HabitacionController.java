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

    @PatchMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacion(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        service.actualizarEstadoHabitacion(id, idEstado);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado-sistema/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacionSistema(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        service.actualizarEstadoHabitacionSistema(id, idEstado);
        return ResponseEntity.noContent().build();
    }

}
