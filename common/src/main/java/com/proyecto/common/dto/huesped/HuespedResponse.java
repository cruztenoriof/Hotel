package com.proyecto.common.dto.huesped;

public record HuespedResponse(

        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String documento,
        String nacionalidad
) {
}