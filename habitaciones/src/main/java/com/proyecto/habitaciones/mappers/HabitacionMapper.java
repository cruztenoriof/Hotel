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
    public HabitacionResponse entidadAResponse(Habitacion entity) {
        if (entity == null) return null;
        return new HabitacionResponse(
                entity.getId(),
                entity.getNumero(),
                entity.getTipo(),
                entity.getPrecio(),
                entity.getCapacidad(),
                entity.getEstadoHabitacion().getDescripcion()
        );
    }

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.builder()
                .numero(request.numero())
                .tipo(request.tipo())
                .precio(request.precio())
                .capacidad(request.capacidad())
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

}
