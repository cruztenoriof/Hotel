package com.proyecto.common.enums;

import com.proyecto.common.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoHabitacion {
    DISPONIBLE(1L, "Disponible"),
    OCUPADA(2L, "Ocupada"),
    LIMPIEZA(3L, "En limpieza"),
    MANTENIMIENTO(4L, "En mantenimiento");

    private final Long codigo;
    private final String descripcion;

    public static EstadoHabitacion obtenerEstadoHabitacionPorCodigo(Long codigo) {
        for (EstadoHabitacion e : values()) {
            if (e.codigo.equals(codigo)) {
                return e;
            }
        }
        throw new RecursoNoEncontradoException("Código de estado de habitación no válido: " + codigo);
    }
}

