package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByPagoId(Long pagoId);

    Optional<Factura> findByNumeroFactura(String numeroFactura);

    boolean existsByNumeroFactura(String numeroFactura);

    boolean existsByPagoId(Long pagoId);
}
