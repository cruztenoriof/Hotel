package com.proyecto.reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proyecto.common.dto.habitacion.DatosHabitacion;
import com.proyecto.common.dto.huesped.DatosHuesped;

import java.time.LocalDate;

public record ReservaResponse(
		Long id,
		Long idHuesped,
		Long idHabitacion,
		DatosHuesped huesped,
		DatosHabitacion habitacion,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		LocalDate fechaEntrada,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		LocalDate fechaSalida,
		String estadoReserva
) {}
