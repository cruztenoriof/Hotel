package com.proyecto.reservas.entities;

import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.utils.ValoresUnicosUtils;
import com.proyecto.reservas.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

	public void actualizarEstadoReserva(EstadoReserva nuevoEstado) {
		if (!this.estadoReserva.puedeCambiar().contains(nuevoEstado)) {
			throw new IllegalStateException("La reserva con estado " + this.estadoReserva +
					" solo puede cambiar a " + this.estadoReserva.puedeCambiar());
		}
		this.estadoReserva = nuevoEstado;
	}

	public static Reserva crear(
			Long idHabitacion, Long idHuesped, LocalDate fechaEntrada, LocalDate fechaSalida){

		validarDatos(idHabitacion, idHuesped, fechaEntrada, fechaSalida);

		return Reserva.builder()
				.idHabitacion(idHabitacion)
				.idHuesped(idHuesped)
				.fechaEntrada(fechaEntrada)
				.fechaSalida(fechaSalida)
				.estadoReserva(EstadoReserva.CONFIRMADA)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	private static void validarDatos(Long idHabitacion,
									 Long idHuesped,
									 LocalDate fechaEntrada,
									 LocalDate fechaSalida){
		validarFechas(fechaEntrada, fechaSalida);
	}


	private static void validarFechas(LocalDate entrada, LocalDate salida){
		if(entrada == null)
			throw new IllegalArgumentException("La fecha de entrada es requerida");
		if(salida == null)
			throw new IllegalArgumentException("La fecha de salida es requerida");
		if(!entrada.isBefore(salida))
			throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida");
		if(entrada.isBefore(LocalDate.now()))
			throw new IllegalArgumentException("La fecha de entrada no puede ser anterior al día actual");

	}


	public void actualizar(LocalDate nuevaFechaEntrada, LocalDate nuevaFechaSalida) {

		this.puedeActualizar();
		validarFechas(nuevaFechaEntrada, nuevaFechaSalida);
		if (this.estadoReserva == EstadoReserva.EN_CURSO
				&& !this.fechaEntrada.isEqual(nuevaFechaEntrada)) {
			throw new IllegalStateException(
					"Una reserva con estado " + EstadoReserva.EN_CURSO
							+ " solo permite modificar la fecha de salida");
		}
/*
		if (!nuevaFechaEntrada.isBefore(nuevaFechaSalida)) {
			throw new IllegalArgumentException(
					"La fecha de entrada debe ser anterior a la fecha de salida");
		}
*/
		this.fechaEntrada = nuevaFechaEntrada;
		this.fechaSalida = nuevaFechaSalida;
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
