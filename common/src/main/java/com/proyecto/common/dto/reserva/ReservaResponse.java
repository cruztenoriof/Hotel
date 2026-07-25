package com.proyecto.common.dto.reserva;

import com.proyecto.common.dto.habitacion.DatosHabitacion;
import com.proyecto.common.dto.huesped.DatosHuesped;

import java.time.LocalDate;

public record ReservaResponse(

        Long id,
        DatosHuesped huesped,
        DatosHabitacion habitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        String estado

) {
}