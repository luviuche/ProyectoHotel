package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Reserva;
import com.acm.proyectohotel.entidad.ReservaServicio;
import com.acm.proyectohotel.entidad.Servicio;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.ReservaRepository;
import com.acm.proyectohotel.repositorio.ReservaServicioRepository;
import com.acm.proyectohotel.repositorio.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ReservaServicioService {

    private final ReservaServicioRepository reservaServicioRepository;
    private final ReservaRepository reservaRepository;
    private final ServicioRepository servicioRepository;

    public ReservaServicioService(ReservaServicioRepository reservaServicioRepository,
                                  ReservaRepository reservaRepository,
                                  ServicioRepository servicioRepository) {
        this.reservaServicioRepository = reservaServicioRepository;
        this.reservaRepository = reservaRepository;
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservaServicio> listar() {
        return reservaServicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReservaServicio obtener(Long id) {
        return reservaServicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("ReservaServicio", id));
    }

    public ReservaServicio crear(ReservaServicio detalle) {
        detalle.setReserva(resolverReserva(detalle.getReservaId()));
        Servicio servicio = resolverServicio(detalle.getServicioId());
        detalle.setServicio(servicio);
        prepararMontos(detalle, servicio);
        detalle.setId(null);
        return reservaServicioRepository.save(detalle);
    }

    public ReservaServicio actualizar(Long id, ReservaServicio datos) {
        ReservaServicio existente = obtener(id);
        existente.setReserva(resolverReserva(datos.getReservaId()));
        Servicio servicio = resolverServicio(datos.getServicioId());
        existente.setServicio(servicio);
        existente.setCantidad(datos.getCantidad());
        existente.setPrecioUnitario(datos.getPrecioUnitario());
        prepararMontos(existente, servicio);
        return reservaServicioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!reservaServicioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("ReservaServicio", id);
        }
        reservaServicioRepository.deleteById(id);
    }

    /** Toma el precio del servicio cuando no se indica y calcula el subtotal. */
    private void prepararMontos(ReservaServicio detalle, Servicio servicio) {
        if (detalle.getCantidad() == null || detalle.getCantidad() < 1) {
            throw new ReglaNegocioException("La cantidad debe ser mayor o igual a 1.");
        }
        if (detalle.getPrecioUnitario() == null) {
            detalle.setPrecioUnitario(servicio.getPrecio());
        }
        detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
    }

    private Reserva resolverReserva(Long reservaId) {
        if (reservaId == null) {
            throw new ReglaNegocioException("El detalle debe tener un reservaId.");
        }
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));
    }

    private Servicio resolverServicio(Long servicioId) {
        if (servicioId == null) {
            throw new ReglaNegocioException("El detalle debe tener un servicioId.");
        }
        return servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio", servicioId));
    }
}
