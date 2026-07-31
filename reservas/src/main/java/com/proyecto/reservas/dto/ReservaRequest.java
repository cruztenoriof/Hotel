package com.proyecto.reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ReservaRequest(

		@NotNull(message = "El id del huésped es requerido")
		@Positive(message = "El id del huésped debe ser positivo")
		Long idHuesped,

		@NotNull(message = "El id de la habitación es requerido")
		@Positive(message = "El id de la habitación debe ser positivo")
		Long idHabitacion,

		@NotNull(message = "La fecha de entrada es requerida")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		LocalDate fechaEntrada,

		@NotNull(message = "La fecha de salida es requerida")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		LocalDate fechaSalida
) {}
