package com.proyecto.reservas.repositories;

import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);

	Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

	boolean existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
			Long idHuesped, EstadoRegistro estadoRegistro, List<EstadoReserva> estadosReserva);

}
