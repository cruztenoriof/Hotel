package com.proyecto.habitaciones.entity;

import com.proyecto.common.enums.EstadoHabitacion;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.utils.StringCustomUtils;
import com.proyecto.common.utils.ValoresUnicosUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "HABITACIONES")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "TIPO", length = 30, nullable = false)
    private String tipo;

    @Column(name = "PRECIO", precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION", nullable = false)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void actualizar(Integer numero, String tipo, BigDecimal precio, Integer capacidad) {
        validarActualizacionPermitida();
        validarDatos(numero, tipo, precio, capacidad);

        this.numero = numero;
        this.tipo = tipo.trim();
        this.precio = precio;
        this.capacidad = capacidad;
    }

    public void eliminar() {
        validarEliminacionPermitida();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
    public void actualizarEstadoSistema(EstadoHabitacion nuevoEstado) {
        validarNoEliminado();
        this.estadoHabitacion = nuevoEstado;
    }

    public static Habitacion crear(Integer numero, String tipo, BigDecimal precio, Integer capacidad){
        validarDatos(numero, tipo, precio, capacidad);
        return Habitacion.builder()
                .numero(numero)
                .tipo(tipo.trim())
                .precio(precio)
                .capacidad(capacidad)
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    private static void validarDatos(Integer numero, String tipo, BigDecimal precio, Integer capacidad){
        ValoresUnicosUtils.validarEnteroPositivo(
                numero, "El número de habitación es requerido y debe ser positivo");
        StringCustomUtils.validarTamanio(
                tipo, 3, 30, "El tipo de habitación es requerido");
        ValoresUnicosUtils.validarBigDecimalPositivo(
                precio, "El precio es requerido y debe ser mayor a cero");
        if(capacidad > 10)
            throw new IllegalArgumentException("La capacidad máxima es de 10 personas");
        ValoresUnicosUtils.validarEnteroPositivo(capacidad, "La capacidad es requerida y debe ser positiva");
    }

    private void validarNoEliminado(){
        if(this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("La habitación ya está eliminada");
    }

    private void validarActualizacionPermitida(){
        validarNoEliminado();
        if(this.estadoHabitacion == EstadoHabitacion.OCUPADA)
            throw new IllegalArgumentException("Una habitación ocupada no puede actualizarse");
    }

    private void validarEliminacionPermitida(){
        validarNoEliminado();
        if(this.estadoHabitacion == EstadoHabitacion.OCUPADA)
            throw new IllegalArgumentException("La habitación con estado " + this.estadoHabitacion + " no puede eliminarse");
    }
























}