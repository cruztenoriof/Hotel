package com.proyecto.reservas.services;


import com.proyecto.common.clients.HabitacionCliente;
import com.proyecto.common.clients.HuespedCliente;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.common.dto.huesped.HuespedResponse;
import com.proyecto.common.enums.EstadoHabitacion;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.exceptions.EntidadRelacionadaException;
import com.proyecto.common.exceptions.RecursoNoEncontradoException;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;
import com.proyecto.reservas.mappers.ReservaMapper;
import com.proyecto.reservas.repositories.ReservaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ReservaServiceImpl implements ReservaService {

	private final ReservaRepository reservaRepository;

	private final ReservaMapper reservaMapper;

	private final HuespedCliente huespedClient;

	private final HabitacionCliente habitacionClient;

	private final List<EstadoReserva> ESTADOS_INVALIDOS_ELIMINAR_HUESPED =
			List.of(EstadoReserva.EN_CURSO);

	@Override
	@Transactional(readOnly = true)
	public List<ReservaResponse> listar() {
		log.info("Listado de todas las Reservas activas solicitado");
		return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(reserva ->
					reservaMapper.entidadAResponse(
							reserva,
							obtenerHuespedSinEstado(reserva.getIdHuesped()),
							obtenerHabitacionSinEstado(reserva.getIdHabitacion()))
				).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ReservaResponse obtenerPorId(Long id) {
		Reserva reserva = obtenerReservaActivaOException(id);
		return reservaMapper.entidadAResponse(
				reserva,
				obtenerHuespedSinEstado(reserva.getIdHuesped()),
				obtenerHabitacionSinEstado(reserva.getIdHabitacion()));
	}

	@Override
	@Transactional(readOnly = true)
	public void huespedTieneReservaEnCurso(Long idHuesped) {
		log.info("Validando reservas con estados {} para el huésped con id: {}",
				ESTADOS_INVALIDOS_ELIMINAR_HUESPED, idHuesped);

		boolean tieneReservas = reservaRepository
				.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
						idHuesped,
						EstadoRegistro.ACTIVO,
						ESTADOS_INVALIDOS_ELIMINAR_HUESPED);

		if (tieneReservas)
			throw new EntidadRelacionadaException(
					"No se puede eliminar el huésped ya que tiene una reserva con estado: "
							+ ESTADOS_INVALIDOS_ELIMINAR_HUESPED);
	}

	@Override
	public ReservaResponse registrar(ReservaRequest request) {
		log.info("Registrando nueva Reserva: {}", request);

		validarFechas(request.fechaEntrada(), request.fechaSalida());

		HuespedResponse huesped = obtenerHuespedActivo(request.idHuesped());

		HabitacionResponse habitacion = obtenerHabitacionActiva(request.idHabitacion());

		validarHabitacionDisponible(habitacion);

		Reserva reserva = reservaRepository.save(reservaMapper.requestAEntidad(request));

		cambiarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.OCUPADA);

		log.info("Reserva registrada exitosamente: {}", reserva.getId());
		return reservaMapper.entidadAResponse(reserva, huesped, habitacion);
	}

	@Override
	public ReservaResponse actualizar(ReservaRequest request, Long id) {
		Reserva reserva = obtenerReservaActivaOException(id);
		log.info("Actualizando Reserva con id: {}", reserva.getId());

		validarNoCambiaHuespedNiHabitacion(request, reserva);

		reserva.actualizar(request.fechaEntrada(), request.fechaSalida());

		HuespedResponse huesped = obtenerHuespedSinEstado(reserva.getIdHuesped());
		HabitacionResponse habitacion = obtenerHabitacionSinEstado(reserva.getIdHabitacion());

		log.info("Reserva actualizada con id: {}", reserva.getId());
		return reservaMapper.entidadAResponse(reserva, huesped, habitacion);
	}

	@Override
	public void eliminar(Long id) {
		Reserva reserva = obtenerReservaActivaOException(id);
		log.info("Eliminando Reserva con id: {}", id);

		reserva.eliminar();

		log.info("Reserva con id {} ha sido marcada como eliminada", id);
	}

	@Override
	public void actualizarEstadoReserva(Long idReserva, Long idEstadoReserva) {
		Reserva reserva = obtenerReservaActivaOException(idReserva);
		log.info("Actualizando estado de la reserva: {}", reserva.getId());

		EstadoReserva nuevoEstado = EstadoReserva.obtenerEstadoReservaPorCodigo(idEstadoReserva);
		reserva.actualizarEstadoReserva(nuevoEstado);

		aplicarEfectoSobreHabitacion(reserva.getIdHabitacion(), nuevoEstado);

		log.info("Estado de la reserva {} actualizado correctamente a {}", reserva.getId(), nuevoEstado);
	}

	private Reserva obtenerReservaActivaOException(Long id) {
		log.info("Buscando Reserva activa con id: {}", id);
		return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
				() -> new RecursoNoEncontradoException("Reserva activa no encontrada con el id: " + id));
	}

	private HuespedResponse obtenerHuespedActivo(Long idHuesped) {
		log.info("Buscando huésped activo con id {} en el servicio remoto...", idHuesped);
		return huespedClient.obtenerHuespedActivoPorId(idHuesped);
	}

	private HuespedResponse obtenerHuespedSinEstado(Long idHuesped) {
		log.info("Buscando huésped sin estado con id {} en el servicio remoto...", idHuesped);
		return huespedClient.obtenerHuespedPorIdSinEstado(idHuesped);
	}

	private HabitacionResponse obtenerHabitacionActiva(Long idHabitacion) {
		log.info("Buscando habitación activa con id {} en el servicio remoto...", idHabitacion);
		return habitacionClient.obtenerHabitacionActivaPorId(idHabitacion);
	}

	private HabitacionResponse obtenerHabitacionSinEstado(Long idHabitacion) {
		log.info("Buscando habitación sin estado con id {} en el servicio remoto...", idHabitacion);
		return habitacionClient.obtenerHabitacionPorIdSinEstado(idHabitacion);
	}

	private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
		if (!fechaEntrada.isBefore(fechaSalida)) {
			throw new IllegalArgumentException(
					"La fecha de entrada debe ser anterior a la fecha de salida");
		}
	}

	private void validarHabitacionDisponible(HabitacionResponse habitacion) {
		log.info("Validando si la habitación se encuentra en estado: {}", EstadoHabitacion.DISPONIBLE);

		if (!EstadoHabitacion.DISPONIBLE.getDescripcion().equalsIgnoreCase(habitacion.estado()))
			throw new IllegalStateException(
					"La habitación no se encuentra en estado " + EstadoHabitacion.DISPONIBLE);
	}

	private void validarNoCambiaHuespedNiHabitacion(ReservaRequest request, Reserva reserva) {
		if (!reserva.getIdHuesped().equals(request.idHuesped())) {
			throw new IllegalStateException("No se puede cambiar el huésped de una reserva existente");
		}
		if (!reserva.getIdHabitacion().equals(request.idHabitacion())) {
			throw new IllegalStateException("No se puede cambiar la habitación de una reserva existente");
		}
	}

	private void aplicarEfectoSobreHabitacion(Long idHabitacion, EstadoReserva nuevoEstado) {
		switch (nuevoEstado) {

			case EN_CURSO ->
				// Check-in: la habitación permanece OCUPADA, no requiere cambio.
				log.info("Check-in realizado, la habitación {} permanece {}", idHabitacion, EstadoHabitacion.OCUPADA);

			case FINALIZADA, CANCELADA ->
				cambiarEstadoHabitacion(idHabitacion, EstadoHabitacion.DISPONIBLE);

			case CONFIRMADA -> {
				// No es un destino de transición alcanzable; no requiere acción.
			}
		}
	}

	private void cambiarEstadoHabitacion(Long idHabitacion, EstadoHabitacion nuevoEstado) {
		log.info("Actualizando estado (sistema) de la habitación con id {} a {}", idHabitacion, nuevoEstado);
		habitacionClient.actualizarEstadoHabitacionSistema(idHabitacion, nuevoEstado.getCodigo());
	}

}
