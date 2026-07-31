package com.proyecto.common.clients;

import com.proyecto.common.dto.habitacion.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "habitaciones")
    public interface HabitacionCliente {

        @GetMapping("/{id}")
        HabitacionResponse obtenerHabitacionActivaPorId(@PathVariable Long id);

        @GetMapping("/id-habitacion/{id}")
        HabitacionResponse obtenerHabitacionPorIdSinEstado(@PathVariable Long id);

        @PatchMapping("/{id}/estado-sistema/{idEstado}")
        void actualizarEstadoHabitacionSistema(@PathVariable Long id, @PathVariable Long idEstado);
    }
