package com.proyecto.reservas.mappers;

import com.proyecto.common.dto.habitacion.DatosHabitacion;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.common.dto.huesped.DatosHuesped;
import com.proyecto.common.dto.huesped.HuespedResponse;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.mapper.CommonMapper;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva> {

	@Override
	public Reserva requestAEntidad(ReservaRequest request) {
		if (request == null) return null;

		return Reserva.builder()
				.idHuesped(request.idHuesped())
				.idHabitacion(request.idHabitacion())
				.fechaEntrada(request.fechaEntrada())
				.fechaSalida(request.fechaSalida())
				.estadoReserva(EstadoReserva.CONFIRMADA)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public ReservaResponse entidadAResponse(Reserva entidad) {
		if (entidad == null) return null;

		return new ReservaResponse(
				entidad.getId(),
				null,
				null,
				entidad.getFechaEntrada(),
				entidad.getFechaSalida(),
				entidad.getEstadoReserva().getDescripcion());
	}

	public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
		if (entidad == null) return null;

		return new ReservaResponse(
				entidad.getId(),
				this.huespedResponseADatosHuesped(huesped),
				this.habitacionResponseADatosHabitacion(habitacion),
				entidad.getFechaEntrada(),
				entidad.getFechaSalida(),
				entidad.getEstadoReserva().getDescripcion());
	}

	private DatosHuesped huespedResponseADatosHuesped(HuespedResponse huesped) {
		if (huesped == null) return null;

		return new DatosHuesped(
				huesped.nombre(),
				huesped.idDocumento(),
				huesped.telefono()
		);
	}

	private DatosHabitacion habitacionResponseADatosHabitacion(HabitacionResponse habitacion) {
		if (habitacion == null) return null;

		return new DatosHabitacion(
				habitacion.numero(),
				habitacion.tipo(),
				habitacion.precio()
		);
	}

}
