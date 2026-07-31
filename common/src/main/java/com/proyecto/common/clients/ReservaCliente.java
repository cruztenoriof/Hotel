package com.proyecto.common.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaCliente {


    @GetMapping("/id-huesped/{idHuesped}/reserva-en-curso")
    void huespedTieneReservaEnCurso(@PathVariable Long idHuesped);
}