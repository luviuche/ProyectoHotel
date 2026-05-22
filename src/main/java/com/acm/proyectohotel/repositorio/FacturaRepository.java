package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Guion bajo: fuerza la travesia pago.id (evita colision con getPagoId()).
    Optional<Factura> findByPago_Id(Long pagoId);

    Optional<Factura> findByNumeroFactura(String numeroFactura);

    boolean existsByNumeroFactura(String numeroFactura);

    boolean existsByPago_Id(Long pagoId);
}
