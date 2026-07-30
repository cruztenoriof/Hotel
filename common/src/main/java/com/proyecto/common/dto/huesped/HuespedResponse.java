package com.proyecto.common.dto.huesped;

public record HuespedResponse(

        Long id,
        String nombre,
        String email,
        String telefono,
        String tipoDocumento,
        String idDocumento,
        String nacionalidad,
        String estadoRegistro
) {
}