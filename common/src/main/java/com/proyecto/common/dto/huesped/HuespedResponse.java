package com.proyecto.common.dto.huesped;

public record HuespedResponse(

        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        String telefono,
        String tipoDocumento,
        String idDocumento,
        String nacionalidad
) {
}