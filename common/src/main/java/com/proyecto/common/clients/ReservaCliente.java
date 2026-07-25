package com.proyecto.common.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "reservas")
public interface ReservaCliente {
}
