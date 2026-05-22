package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Factura;
import com.acm.proyectohotel.entidad.Pago;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.FacturaRepository;
import com.acm.proyectohotel.repositorio.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PagoRepository pagoRepository;

    public FacturaService(FacturaRepository facturaRepository, PagoRepository pagoRepository) {
        this.facturaRepository = facturaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Transactional(readOnly = true)
    public List<Factura> listar() {
        return facturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Factura obtener(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura", id));
    }

    public Factura crear(Factura factura) {
        Pago pago = resolverPago(factura.getPagoId());
        if (facturaRepository.existsByPagoId(pago.getId())) {
            throw new ReglaNegocioException("El pago " + pago.getId() + " ya tiene una factura asociada.");
        }
        if (facturaRepository.existsByNumeroFactura(factura.getNumeroFactura())) {
            throw new ReglaNegocioException(
                    "Ya existe una factura con el numero '" + factura.getNumeroFactura() + "'.");
        }
        factura.setPago(pago);
        prepararValores(factura);
        factura.setId(null);
        return facturaRepository.save(factura);
    }

    public Factura actualizar(Long id, Factura datos) {
        Factura existente = obtener(id);
        Pago pago = resolverPago(datos.getPagoId());
        if (!existente.getPago().getId().equals(pago.getId())
                && facturaRepository.existsByPagoId(pago.getId())) {
            throw new ReglaNegocioException("El pago " + pago.getId() + " ya tiene una factura asociada.");
        }
        if (!existente.getNumeroFactura().equals(datos.getNumeroFactura())
                && facturaRepository.existsByNumeroFactura(datos.getNumeroFactura())) {
            throw new ReglaNegocioException(
                    "Ya existe una factura con el numero '" + datos.getNumeroFactura() + "'.");
        }
        existente.setPago(pago);
        existente.setNumeroFactura(datos.getNumeroFactura());
        existente.setSubtotal(datos.getSubtotal());
        existente.setImpuestos(datos.getImpuestos());
        existente.setTotal(datos.getTotal());
        existente.setDescripcion(datos.getDescripcion());
        if (datos.getFechaEmision() != null) {
            existente.setFechaEmision(datos.getFechaEmision());
        }
        prepararValores(existente);
        return facturaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Factura", id);
        }
        facturaRepository.deleteById(id);
    }

    /** Calcula el total a partir de subtotal + impuestos cuando no se envia, y fija la fecha. */
    private void prepararValores(Factura factura) {
        BigDecimal subtotal = factura.getSubtotal() != null ? factura.getSubtotal() : BigDecimal.ZERO;
        BigDecimal impuestos = factura.getImpuestos() != null ? factura.getImpuestos() : BigDecimal.ZERO;
        factura.setSubtotal(subtotal);
        factura.setImpuestos(impuestos);
        if (factura.getTotal() == null) {
            factura.setTotal(subtotal.add(impuestos));
        }
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDateTime.now());
        }
    }

    private Pago resolverPago(Long pagoId) {
        if (pagoId == null) {
            throw new ReglaNegocioException("La factura debe tener un pagoId.");
        }
        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago", pagoId));
    }
}
