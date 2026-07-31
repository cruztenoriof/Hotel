package com.proyecto.reservas.controllers;

import com.proyecto.common.controller.CommonController;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.services.ReservaService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ReservaController extends CommonController<ReservaRequest, ReservaResponse, ReservaService> {

	public ReservaController(ReservaService service) {
		super(service);
	}

	@PatchMapping("/{idReserva}/estado/{idEstado}")
	public ResponseEntity<Void> actualizarEstadoReserva(
			@PathVariable @Positive(message = "El idReserva debe ser positivo") Long idReserva,
			@PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {

		service.actualizarEstadoReserva(idReserva, idEstado);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/id-huesped/{idHuesped}/reserva-en-curso")
	public ResponseEntity<Void> huespedTieneReservaEnCurso(
			@PathVariable @Positive(message = "El idHuesped debe ser positivo") Long idHuesped) {

		service.huespedTieneReservaEnCurso(idHuesped);
		return ResponseEntity.noContent().build();
	}

}
