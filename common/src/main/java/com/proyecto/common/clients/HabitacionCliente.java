package com.proyecto.common.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "habitaciones")
public interface HabitacionCliente {
}
