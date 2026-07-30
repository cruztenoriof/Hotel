package com.francisco.huespedes.service;

import com.proyecto.common.dto.huesped.HuespedRequest;
import com.proyecto.common.dto.huesped.HuespedResponse;
import com.proyecto.common.service.CrudService;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {

    HuespedResponse obtenerHuespedPorIdSinEstado (Long id);
}
