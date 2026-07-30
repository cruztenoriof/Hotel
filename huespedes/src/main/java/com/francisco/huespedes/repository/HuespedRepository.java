package com.francisco.huespedes.repository;


import com.francisco.huespedes.entity.Huespedes;
import com.proyecto.common.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huespedes, Long> {

    List<Huespedes> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Huespedes> findByIdHuespedAndEstadoRegistro(Long idHuesped, EstadoRegistro estadoRegistro);

    boolean existsByEmailAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);

    boolean existsByIdDocumentoAndEstadoRegistro(String idDocumento, EstadoRegistro estadoRegistro);

    boolean existsByIdDocumentoAndEstadoRegistroAndIdHuespedNot(String IdDocumento,
                                                                EstadoRegistro estadoRegistro, Long idHuesped);

    boolean existsByEmailAndEstadoRegistroAndIdHuespedNot(String email,
                                                          EstadoRegistro estadoRegistro, Long idHuesped);

    boolean existsByTelefonoAndEstadoRegistroAndIdHuespedNot(String telefono,
                                                             EstadoRegistro estadoRegistro, Long idHuesped);
}