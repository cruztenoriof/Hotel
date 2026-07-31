package com.proyecto.habitaciones.repository;

import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    List<Habitacion> findByEstadoRegistro(com.proyecto.common.enums.EstadoRegistro estado);

    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estado);

    boolean existsByNumeroAndIdNotAndEstadoRegistro(Integer numero, Long id, EstadoRegistro estado);
}
