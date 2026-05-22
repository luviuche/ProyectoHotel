package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Reserva;
import com.acm.proyectohotel.entidad.Usuario;
import com.acm.proyectohotel.enums.EstadoReserva;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.ReservaRepository;
import com.acm.proyectohotel.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reserva obtener(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));
    }

    public Reserva crear(Reserva reserva) {
        reserva.setCliente(resolverCliente(reserva.getClienteId()));
        validarFechas(reserva);
        validarTotal(reserva);
        if (reserva.getEstado() == null) {
            reserva.setEstado(EstadoReserva.PENDIENTE);
        }
        reserva.setId(null);
        return reservaRepository.save(reserva);
    }

    public Reserva actualizar(Long id, Reserva datos) {
        Reserva existente = obtener(id);
        existente.setCliente(resolverCliente(datos.getClienteId()));
        existente.setFechaEntrada(datos.getFechaEntrada());
        existente.setFechaSalida(datos.getFechaSalida());
        existente.setEstado(datos.getEstado() != null ? datos.getEstado() : existente.getEstado());
        existente.setTotal(datos.getTotal());
        validarFechas(existente);
        validarTotal(existente);
        return reservaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Reserva", id);
        }
        reservaRepository.deleteById(id);
    }

    private void validarFechas(Reserva reserva) {
        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null) {
            throw new ReglaNegocioException("La reserva requiere fecha de entrada y de salida.");
        }
        if (!reserva.getFechaSalida().isAfter(reserva.getFechaEntrada())) {
            throw new ReglaNegocioException("La fecha de salida debe ser posterior a la de entrada.");
        }
    }

    private void validarTotal(Reserva reserva) {
        if (reserva.getTotal() == null || reserva.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("El total de la reserva no puede ser negativo.");
        }
    }

    private Usuario resolverCliente(Long clienteId) {
        if (clienteId == null) {
            throw new ReglaNegocioException("La reserva debe tener un clienteId.");
        }
        return usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", clienteId));
    }
}
