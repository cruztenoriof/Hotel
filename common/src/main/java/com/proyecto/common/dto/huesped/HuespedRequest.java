package com.proyecto.common.dto.huesped;

import jakarta.validation.constraints.*;

public record HuespedRequest(

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
        String nombre,

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
        String apellidoPaterno,

        @NotBlank(message = "El apellido es materno es obligatorio.")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
        String apellidoMaterno,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
        String email,

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos.")
        String telefono,

        @NotNull(message = "El documento es obligatorio.")
        //@Size(min = 1, max = 30, message = "El documento debe tener entre 3 y 30 caracteres.")
        Long tipoDocumento,

        @NotBlank(message = "El id del documento es obligatorio.")
        @Size(min = 3, max = 30, message = "El documento debe tener entre 3 y 30 caracteres.")
        String idDocumento,

        @NotBlank(message = "La nacionalidad es obligatoria.")
        @Size(max = 50, message = "La nacionalidad no puede superar los 50 caracteres.")
        String nacionalidad
) {
}