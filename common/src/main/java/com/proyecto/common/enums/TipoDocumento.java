package com.proyecto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TipoDocumento {
    INE (1L, "INE"),
    PASAPORTE (2L, "Pasaporte"),
    LICENCIA_DE_CONDUCIR (3L, "Licencia de conducir");

    private final Long codigo;
    private final String descripcion;

    public static TipoDocumento obtenerTipoDocumentoporCodigo(Long codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("El ID del documento no puede ser nulo.");
        }
        return buscarPorCodigo(String.valueOf(codigo));
    }
    public static TipoDocumento buscarPorCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo.");
        }
        for (TipoDocumento disp : values()) {
            if (String.valueOf(disp.getCodigo()).equals(codigo.trim())) {
                return disp;
            }
        }
        throw new IllegalArgumentException("El código de documento proporcionado no existe: " + codigo);
    }
}
