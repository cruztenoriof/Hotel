package com.proyecto.habitaciones.service;

import com.proyecto.common.dto.habitacion.HabitacionRequest;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.common.service.CrudService;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    void actualizarEstadoHabitacion(Long id, Long idEstado);

    void actualizarEstadoHabitacionSistema(Long id, Long idEstado);

}
