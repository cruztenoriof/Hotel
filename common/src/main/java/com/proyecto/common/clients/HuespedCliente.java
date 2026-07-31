package com.proyecto.common.clients;

import com.proyecto.common.dto.huesped.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "huespedes")
public interface HuespedCliente {

    @GetMapping("/{id}")
    HuespedResponse obtenerHuespedActivoPorId(@PathVariable Long id);

    @GetMapping("/id-huesped/{id}")
    HuespedResponse obtenerHuespedPorIdSinEstado(@PathVariable Long id);
}