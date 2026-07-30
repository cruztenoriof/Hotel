package com.francisco.huespedes.controller;

import com.francisco.huespedes.service.HuespedService;
import com.proyecto.common.controller.CommonController;
import com.proyecto.common.dto.huesped.HuespedRequest;
import com.proyecto.common.dto.huesped.HuespedResponse;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/huespedes")
public class HuespedController extends CommonController <HuespedRequest,HuespedResponse, HuespedService> {
    public HuespedController(HuespedService service) {

        super(service);
    }
    @GetMapping("/id-huesped/{id}")
    public ResponseEntity<HuespedResponse> obtenerHuespedPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser postivo") Long id) {
        return ResponseEntity.ok(service.obtenerHuespedPorIdSinEstado(id));
    }
}
