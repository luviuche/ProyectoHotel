package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Pago;
import com.acm.proyectohotel.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByReservaId(Long reservaId);

    boolean existsByReservaId(Long reservaId);

    List<Pago> findByEstado(EstadoPago estado);
}
