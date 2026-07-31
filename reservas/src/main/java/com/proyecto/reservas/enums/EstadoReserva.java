package com.proyecto.reservas.enums;

import com.proyecto.common.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum EstadoReserva {

	CONFIRMADA(1L, "Reserva creada") {
		@Override
		public Set<EstadoReserva> puedeCambiar() {
			return EnumSet.of(EN_CURSO, CANCELADA);
		}
	},

	EN_CURSO(2L, "Check-in realizado") {
		@Override
		public Set<EstadoReserva> puedeCambiar() {
			return EnumSet.of(FINALIZADA);
		}
	},

	FINALIZADA(3L, "Check-out realizado") {
		@Override
		public Set<EstadoReserva> puedeCambiar() {
			return Set.of();
		}
	},

	CANCELADA(4L, "Reserva cancelada") {
		@Override
		public Set<EstadoReserva> puedeCambiar() {
			return Set.of();
		}
	};

	private final Long codigo;
	private final String descripcion;

	public abstract Set<EstadoReserva> puedeCambiar();

	public static EstadoReserva obtenerEstadoReservaPorCodigo(Long codigo) {
		for (EstadoReserva e : values()) {
			if (e.codigo.equals(codigo)) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("Código de estado de reserva no válido: " + codigo);
	}
}
