package com.proyecto.habitaciones.mappers;

import com.proyecto.common.dto.habitacion.HabitacionRequest;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.common.enums.EstadoHabitacion;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.mapper.CommonMapper;
import com.proyecto.habitaciones.entity.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        if (entidad == null) return null;
        return new HabitacionResponse(
                entidad.getId(),
                entidad.getNumero(),
                entidad.getTipo(),
                entidad.getPrecio(),
                entidad.getCapacidad(),
                entidad.getEstadoHabitacion().getDescripcion()
        );
    }

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.crear(
                request.numero(),
                request.tipo(),
                request.precio(),
                request.capacidad()
        );
    }

}
