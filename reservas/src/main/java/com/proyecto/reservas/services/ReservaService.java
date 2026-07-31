package com.proyecto.reservas.services;

import com.proyecto.common.service.CrudService;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse> {

	void actualizarEstadoReserva(Long idReserva, Long idEstadoReserva);

	void huespedTieneReservaEnCurso(Long idHuesped);

}
