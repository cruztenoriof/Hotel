package com.proyecto.reservas.entities;

import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.reservas.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_RESERVA")
	private Long id;

	@Column(name = "ID_HABITACION", nullable = false)
	private Long idHabitacion;

	@Column(name = "ID_HUESPED", nullable = false)
	private Long idHuesped;

	// NOTA: el nombre de columna respeta el DDL ya creado en la base de datos
	// (contiene un error de tipeo: "FECHA_ENTREDA" en vez de "FECHA_ENTRADA").
	@Column(name = "FECHA_ENTRADA", nullable = false)
	private LocalDate fechaEntrada;

	@Column(name = "FECHA_SALIDA", nullable = false)
	private LocalDate fechaSalida;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_RESERVA", nullable = false)
	private EstadoReserva estadoReserva;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_REGISTRO", nullable = false)
	private EstadoRegistro estadoRegistro;

	/**
	 * Actualiza las fechas de la reserva respetando las reglas de modificación:
	 * - CONFIRMADA (sin check-in): se pueden modificar ambas fechas.
	 * - EN_CURSO (con check-in): solo se puede modificar la fecha de salida;
	 *   la fecha de entrada debe permanecer igual.
	 * - FINALIZADA o CANCELADA: no se permite ninguna modificación.
	 */
	public void actualizar(LocalDate nuevaFechaEntrada, LocalDate nuevaFechaSalida) {

		this.puedeActualizar();

		if (this.estadoReserva == EstadoReserva.EN_CURSO
				&& !this.fechaEntrada.isEqual(nuevaFechaEntrada)) {
			throw new IllegalStateException(
					"Una reserva con estado " + EstadoReserva.EN_CURSO
					+ " solo permite modificar la fecha de salida");
		}

		if (!nuevaFechaEntrada.isBefore(nuevaFechaSalida)) {
			throw new IllegalArgumentException(
					"La fecha de entrada debe ser anterior a la fecha de salida");
		}

		this.fechaEntrada = nuevaFechaEntrada;
		this.fechaSalida = nuevaFechaSalida;
	}

	/**
	 * Transición de estado de la reserva (check-in, check-out, cancelación),
	 * validada contra las transiciones permitidas de {@link EstadoReserva}.
	 */
	public void actualizarEstadoReserva(EstadoReserva nuevoEstado) {
		if (!this.estadoReserva.puedeCambiar().contains(nuevoEstado)) {
			throw new IllegalStateException("La reserva con estado " + this.estadoReserva +
					" solo puede cambiar a " + this.estadoReserva.puedeCambiar());
		}
		this.estadoReserva = nuevoEstado;
	}

	public void eliminar() {
		this.puedeEliminar();
		this.estadoRegistro = EstadoRegistro.ELIMINADO;
	}

	private void puedeActualizar() {
		if (this.estadoReserva == EstadoReserva.FINALIZADA
				|| this.estadoReserva == EstadoReserva.CANCELADA) {
			throw new IllegalStateException(
					"Una reserva con estado " + this.estadoReserva
					+ " no permite ninguna modificación, solo consulta histórica");
		}
	}

	private void puedeEliminar() {
		if (this.estadoReserva == EstadoReserva.CONFIRMADA
				|| this.estadoReserva == EstadoReserva.EN_CURSO) {
			throw new IllegalStateException("Una reserva con estado " +
					EstadoReserva.CONFIRMADA + " o " + EstadoReserva.EN_CURSO +
					" no se puede eliminar");
		}
	}

}
