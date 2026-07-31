package com.proyecto.common.dto.habitacion;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record HabitacionRequest(

        @NotNull(message = "El número de habitación es requerido")
        @Positive(message = "El número de habitación debe ser positivo")
        Integer numero,

        @NotBlank(message = "El tipo de habitación es requerido")
        @Size(min = 1, max = 30, message = "El tipo de habitación debe tener entre 1 y 30 caracteres")
        String tipo,

        @NotNull(message = "El precio es requerido")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mínima es de 1 persona")
        Integer capacidad
) {
}