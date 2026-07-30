package com.francisco.huespedes.mapper;

import com.francisco.huespedes.entity.Huespedes;
import com.proyecto.common.dto.huesped.HuespedRequest;
import com.proyecto.common.dto.huesped.HuespedResponse;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.enums.TipoDocumento;
import com.proyecto.common.mapper.CommonMapper;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper implements CommonMapper <HuespedRequest, HuespedResponse, Huespedes> {
    @Override
    public Huespedes requestAEntidad(HuespedRequest request) {
        if (request == null) return null;
        return Huespedes.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .email(request.email().toLowerCase().trim())
                .tipoDocumento(TipoDocumento.valueOf(request.TipoDocumento().trim().toUpperCase()))
                .idDocumento(request.IdDocumento().trim())
                .nacionalidad(request.nacionalidad())
                .telefono(request.telefono().trim())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public HuespedResponse entidadAResponse(Huespedes entidad) {
        if (entidad == null) return null;
        return new HuespedResponse(
                entidad.getIdHuesped(),
                String.join(" ",entidad.getNombre(), entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getTipoDocumento().name(),
                entidad.getIdDocumento(),
                entidad.getNacionalidad(),
                entidad.getEstadoRegistro() !=null ? entidad.getEstadoRegistro().name() :null
        );
    }
}
