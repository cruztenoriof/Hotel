    package com.francisco.huespedes.entity;


    import com.proyecto.common.enums.EstadoRegistro;
    import com.proyecto.common.enums.TipoDocumento;
    import com.proyecto.common.utils.StringCustomUtils;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "HUESPEDES")
    public class Huespedes {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_huesped")
        private Long idHuesped;

        @Column(nullable = false, length = 50)
        private String nombre;

        @Column(name = "apellido_paterno", nullable = false, length = 50)
        private String apellidoPaterno;

        @Column(name = "apellido_materno", nullable = false, length = 50)
        private String apellidoMaterno;

        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @Column(nullable = false, length = 10)
        private String telefono;

        @Enumerated(EnumType.STRING)
        @Column(name = "tipo_documento", nullable = false)
        private TipoDocumento tipoDocumento;

        @Column(name = "id_documento", nullable = false, length = 30)
        private String idDocumento;

        @Column(nullable = false, length = 50)
        private String nacionalidad;

        @Enumerated(EnumType.STRING)
        @Column(name = "estado_registro", nullable = false)
        private EstadoRegistro estadoRegistro;

        public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno,
                               String telefono,String tipoDocumento, String idDocumento,
                               String nacionalidad, String email) {
            validarNoEliminado();

            validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono,
                    idDocumento, nacionalidad);
            this.nombre = nombre.trim();
            this.apellidoPaterno = apellidoPaterno.trim();
            this.apellidoMaterno = apellidoMaterno.trim();
            this.nacionalidad = nacionalidad;
            this.tipoDocumento = TipoDocumento.valueOf(tipoDocumento.trim().toUpperCase());
            this.idDocumento = idDocumento.trim();
            this.email = email.trim().toLowerCase();
            this.telefono = telefono.trim();
        }

        private void validarDatos(
                String nombre, String apellidoPaterno,
                String apellidoMaterno, String email, String telefono, String documento, String nacionalidad) {
            StringCustomUtils.validarTamanio(nombre, 2, 50,
                    "El nombre es requerido y debe tener entre 2 y 50 caracteres");
            StringCustomUtils.validarTamanio(apellidoPaterno, 2, 50,
                    "El apellido paterno es requerido y debe tener entre 2 y 50 caracteres");
            StringCustomUtils.validarTamanio(apellidoMaterno, 2, 50,
                    "El apellido materno es requerido y debe tener entre 2 y 50 caracteres");
            StringCustomUtils.validarTamanio(email, 1, 100,
                    "El email es requerido y debe tener entre 1 y 100 caracteres");
            StringCustomUtils.validarTamanio(telefono, 10, 10,
                    "El telefono es requerido y debe tener 10 caracteres");
            StringCustomUtils.validarTamanio(documento, 1,30,
                    "El documento es requerido y debe tener entre 1 y 30 caracteres");
            StringCustomUtils.validarTamanio(nacionalidad, 1, 50,
                    "La naacionalidad es requerida y debe tener entre 1 y 50 caracteres");
        }

        private void validarNoEliminado() {
            if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
                throw new IllegalArgumentException(
                        "El huesped está elimiado");
        }
        public void eliminar() {
            this.estadoRegistro = EstadoRegistro.ELIMINADO;
        }
    }