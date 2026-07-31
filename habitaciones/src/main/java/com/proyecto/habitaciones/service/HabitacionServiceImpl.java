package com.proyecto.habitaciones.service;

import com.proyecto.common.dto.habitacion.HabitacionRequest;
import com.proyecto.common.dto.habitacion.HabitacionResponse;
import com.proyecto.common.enums.EstadoHabitacion;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.exceptions.RecursoNoEncontradoException;
import com.proyecto.habitaciones.entity.Habitacion;
import com.proyecto.habitaciones.mappers.HabitacionMapper;
import com.proyecto.habitaciones.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;

    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        log.info("Listado de todas las habitaciones activas solicitado");
        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(habitacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        return habitacionMapper.entidadAResponse(obtenerHabitacionActivaOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando Habitación sin estado con id: {}", id);
        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitación no encontrada con el id: " + id)));
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {

        log.info("Registrando nueva Habitación: {}", request.numero());

        validarNumeroUnico(request.numero());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        habitacionRepository.save(habitacion);

        log.info("Habitación registrada con éxito: {}", habitacion.getNumero());
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);
        log.info("Actualizando Habitación con id: {}", id);

        validarCambiosUnicos(request, habitacion);

        habitacion.actualizar(
                request.numero(),
                request.tipo(),
                request.precio(),
                request.capacidad());

        log.info("Habitación actualizada con éxito: {}", id);

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);
        log.info("Eliminando Habitación con id: {}", id);

        habitacion.eliminar();
        log.info("Habitación con id {} ha sido eliminada", id);
    }

    @Override
    public void actualizarEstadoHabitacion(Long id, Long idEstado) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstado);

        log.info("Actualizando estado (manual) de la habitación {} de {} a {}",
                id, habitacion.getEstadoHabitacion(), nuevoEstado);

        habitacion.actualizarEstadoManual(nuevoEstado);
    }

    @Override
    public void actualizarEstadoHabitacionSistema(Long id, Long idEstado) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstado);

        log.info("Actualizando estado (sistema) de la habitación {} de {} a {}",
                id, habitacion.getEstadoHabitacion(), nuevoEstado);

        habitacion.actualizarEstadoSistema(nuevoEstado);
    }

    private Habitacion obtenerHabitacionActivaOException(Long id) {
        log.info("Buscando Habitación con estado {} con id: {}", EstadoRegistro.ACTIVO, id);
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitación activa no encontrada con el id: " + id));
    }

    private void validarNumeroUnico(Integer numero) {
        if (habitacionRepository.existsByNumeroAndEstadoRegistro(numero, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe una Habitación registrada con el número: " + numero);
        }
    }

    private void validarCambiosUnicos(HabitacionRequest request, Habitacion habitacion) {
        if (!habitacion.getNumero().equals(request.numero()) &&
                habitacionRepository.existsByNumeroAndEstadoRegistro(request.numero(), EstadoRegistro.ACTIVO)) {

            throw new IllegalArgumentException("Ya existe una Habitación registrada con el número: " + request.numero());
        }
    }

}
