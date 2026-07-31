package com.proyecto.habitaciones.entity;

import com.proyecto.common.enums.EstadoHabitacion;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.utils.StringCustomUtils;
import com.proyecto.common.utils.ValoresUnicosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.capacidad = capacidad;
    }

    public void eliminar() {
        this.puedeEliminar();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    /**
     * Cambio de estado MANUAL (disparado por un administrador vía el endpoint
     * público). No permite volver a DISPONIBLE si la habitación está OCUPADA.
     */
    public void actualizarEstadoManual(EstadoHabitacion nuevoEstado) {
        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA
                && nuevoEstado == EstadoHabitacion.DISPONIBLE) {
            throw new IllegalStateException(
                    "No se puede cambiar manualmente a " + EstadoHabitacion.DISPONIBLE
                            + " una habitación que está " + EstadoHabitacion.OCUPADA);
        }
        this.estadoHabitacion = nuevoEstado;
    }

    /**
     * Cambio de estado disparado por el SISTEMA (creación, check-in, check-out
     * o cancelación de una reserva en el microservicio de reservas). No aplica
     * la restricción anterior, ya que es una transición automática de negocio.
     */
    public void actualizarEstadoSistema(EstadoHabitacion nuevoEstado) {
        this.estadoHabitacion = nuevoEstado;
    }

    private void puedeEliminar() {
        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException(
                    "Una habitación con estado " + EstadoHabitacion.OCUPADA + " no se puede eliminar");
        }
    }

}