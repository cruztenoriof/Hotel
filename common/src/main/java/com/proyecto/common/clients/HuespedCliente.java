package com.proyecto.common.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "huespedes")
public interface HuespedCliente {
}
