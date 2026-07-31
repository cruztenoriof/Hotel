package com.proyecto.common.dto.habitacion;

import java.math.BigDecimal;

public record DatosHabitacion(

        Integer numero,
        String tipo,
        BigDecimal precio

) {
}