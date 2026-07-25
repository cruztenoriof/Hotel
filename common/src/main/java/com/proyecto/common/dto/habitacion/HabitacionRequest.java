package com.proyecto.common.dto.habitacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record HabitacionRequest(

        @Positive(message = "El número de habitación debe ser mayor que cero.")
        Integer numero,

        @NotBlank(message = "El tipo es obligatorio.")
        @Size(max = 30)
        String tipo,

        @Positive(message = "El precio debe ser mayor que cero.")
        BigDecimal precio,

        @Min(value = 1, message = "La capacidad mínima es de una persona.")
        Integer capacidad
) {
}