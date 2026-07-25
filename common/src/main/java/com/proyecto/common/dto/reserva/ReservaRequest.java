package com.proyecto.common.dto.reserva;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ReservaRequest(

        @NotNull(message = "El huésped es obligatorio.")
        @Positive(message = "El id del huésped debe ser válido.")
        Long idHuesped,

        @NotNull(message = "La habitación es obligatoria.")
        @Positive(message = "El id de la habitación debe ser válido.")
        Long idHabitacion,

        @NotNull(message = "La fecha de entrada es obligatoria.")
        LocalDate fechaEntrada,

        @NotNull(message = "La fecha de salida es obligatoria.")
        LocalDate fechaSalida
) {
}