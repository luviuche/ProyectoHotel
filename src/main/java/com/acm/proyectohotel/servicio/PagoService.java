package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Pago;
import com.acm.proyectohotel.entidad.Reserva;
import com.acm.proyectohotel.enums.EstadoPago;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.PagoRepository;
import com.acm.proyectohotel.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    public PagoService(PagoRepository pagoRepository, ReservaRepository reservaRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago obtener(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago", id));
    }

    public Pago crear(Pago pago) {
        Reserva reserva = resolverReserva(pago.getReservaId());
        if (pagoRepository.existsByReserva_Id(reserva.getId())) {
            throw new ReglaNegocioException("La reserva " + reserva.getId() + " ya tiene un pago registrado.");
        }
        pago.setReserva(reserva);
        if (pago.getEstado() == null) {
            pago.setEstado(EstadoPago.PENDIENTE);
        }
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        pago.setId(null);
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Long id, Pago datos) {
        Pago existente = obtener(id);
        Reserva reserva = resolverReserva(datos.getReservaId());
        if (!existente.getReserva().getId().equals(reserva.getId())
                && pagoRepository.existsByReserva_Id(reserva.getId())) {
            throw new ReglaNegocioException("La reserva " + reserva.getId() + " ya tiene un pago registrado.");
        }
        existente.setReserva(reserva);
        existente.setMonto(datos.getMonto());
        existente.setMetodoPago(datos.getMetodoPago());
        existente.setEstado(datos.getEstado() != null ? datos.getEstado() : existente.getEstado());
        if (datos.getFechaPago() != null) {
            existente.setFechaPago(datos.getFechaPago());
        }
        return pagoRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pago", id);
        }
        pagoRepository.deleteById(id);
    }

    private Reserva resolverReserva(Long reservaId) {
        if (reservaId == null) {
            throw new ReglaNegocioException("El pago debe tener un reservaId.");
        }
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));
    }
}
