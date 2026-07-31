package com.francisco.huespedes.service;

import com.francisco.huespedes.entity.Huespedes;
import com.francisco.huespedes.mapper.HuespedMapper;
import com.francisco.huespedes.repository.HuespedRepository;
import com.proyecto.common.dto.huesped.HuespedRequest;
import com.proyecto.common.dto.huesped.HuespedResponse;
import com.proyecto.common.enums.EstadoRegistro;
import com.proyecto.common.enums.TipoDocumento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor

public class HuespedServiceImpl implements HuespedService {
    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerHuespedPorIdSinEstado(Long id) {
        log.info("Buscando huesped sin estado con id {}", id);
        return huespedRepository.findById(id)
                .map(huespedMapper::entidadAResponse)
                .orElseThrow(() -> new IllegalArgumentException("Huesped sin estado no encontrado con id: " + id));


    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponse> listar() {
        log.info("Listando todos los huespedes");
        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional
    public HuespedResponse obtenerPorId(Long id) {
        log.info("Buscando huesped activo con ID: {}", id);
        Huespedes huespedes = huespedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Huesped no encontrado con el ID: " + id));
        if (huespedes.getEstadoRegistro() == EstadoRegistro.ELIMINADO) {
            throw new IllegalArgumentException("El huesped con el ID " + id + " está eliminado");
        }
        return huespedMapper.entidadAResponse(huespedes);
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        log.info("Registrando nuevo huesped {}", request.nombre());
        obtenerTipoDocumentoValido(request.tipoDocumento().longValue());
        validarDatosUnicos(request);

        Huespedes huespedes = huespedMapper.requestAEntidad(request);
        Huespedes huespedGuardado = huespedRepository.save(huespedes);
        return huespedMapper.entidadAResponse(huespedGuardado);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, Long id) {
        log.info("Actualizando huesped con id: {}", id);
        Huespedes huespedes = huespedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Huesped no encontrado con el ID " + id));
        if (huespedes.getEstadoRegistro() == EstadoRegistro.ELIMINADO) {
            throw new IllegalArgumentException("No se puede actualizar el huesped con ID " + id + " porque se " +
                    "encuentra eliminado.");
        }
        obtenerTipoDocumentoValido(request.tipoDocumento().longValue());
        //validarReservasActivas(id);
        validarCambiosUnicos(request, id);
        huespedes.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.telefono(),
                request.tipoDocumento(),
                request.idDocumento(),
                request.nacionalidad(),
                request.email()
        );
        Huespedes huespedActualizado = huespedRepository.save(huespedes);
        log.info("Huesped actualizado con éxito: {}", id);
        return huespedMapper.entidadAResponse(huespedActualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando huesped con id: {}", id);
        Huespedes huespedes = huespedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Huesped no encontrado con el ID: " + id));

        if (huespedes.getEstadoRegistro() == EstadoRegistro.ELIMINADO) {
            throw new IllegalArgumentException("El huesped con ID " + id + " ya se encuentra eliminado.");
        }
        //validarReservasActivas(id);
        huespedes.eliminar();
        huespedRepository.save(huespedes);
        log.info("Huesped con id {} ha sido eliminado", id);
    }

    private void validarDatosUnicos(HuespedRequest request) {
        log.info("Validando email único...");
        if (huespedRepository.existsByEmailAndEstadoRegistro(
                request.email().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el email " + request.email());
        }
        log.info("Validando telefono único...");
        if (huespedRepository.existsByTelefonoAndEstadoRegistro(
                request.telefono().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el telefono " +
                    request.telefono());
        }
        log.info("Validando documento único...");
        if (huespedRepository.existsByIdDocumentoAndEstadoRegistro(
                request.idDocumento().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el número de documento: "
                    + request.idDocumento());
        }
    }

    private void validarCambiosUnicos(HuespedRequest request, Long id) {
        log.info("Validando email único...");
        if (huespedRepository.existsByEmailAndEstadoRegistroAndIdHuespedNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el email "
                    + request.email());
        }
        if (huespedRepository.existsByIdDocumentoAndEstadoRegistroAndIdHuespedNot(
                request.idDocumento().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el número de documento." +
                    request.idDocumento());
        }

        if (huespedRepository.existsByTelefonoAndEstadoRegistroAndIdHuespedNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huesped activo registrado con el telefono " +
                    request.telefono());
        }
    }
    private TipoDocumento obtenerTipoDocumentoValido(Long documento) {
        return TipoDocumento.obtenerTipoDocumentoporCodigo(documento);

        /*String normalizado = tipoDocumento.getDescripcion().trim().toUpperCase();
        if (normalizado.equals("INE") || normalizado.equals("PASAPORTE") || normalizado.equals("LICENCIA_DE_CONDUCIR")) {
            return TipoDocumento.valueOf(normalizado);
        }
        throw new IllegalArgumentException(
                "El tipo de documento no es válido. Los únicos documentos válidos son: " +
                        "INE, PASAPORTE y LICENCIA DE CONDUCIR"
        );*/
    }
}
