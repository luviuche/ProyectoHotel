package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Habitacion;
import com.acm.proyectohotel.entidad.Reserva;
import com.acm.proyectohotel.entidad.ReservaHabitacion;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.HabitacionRepository;
import com.acm.proyectohotel.repositorio.ReservaHabitacionRepository;
import com.acm.proyectohotel.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservaHabitacionService {

    private final ReservaHabitacionRepository reservaHabitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;

    public ReservaHabitacionService(ReservaHabitacionRepository reservaHabitacionRepository,
                                    ReservaRepository reservaRepository,
                                    HabitacionRepository habitacionRepository) {
        this.reservaHabitacionRepository = reservaHabitacionRepository;
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservaHabitacion> listar() {
        return reservaHabitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReservaHabitacion obtener(Long id) {
        return reservaHabitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("ReservaHabitacion", id));
    }

    public ReservaHabitacion crear(ReservaHabitacion detalle) {
        detalle.setReserva(resolverReserva(detalle.getReservaId()));
        detalle.setHabitacion(resolverHabitacion(detalle.getHabitacionId()));
        detalle.setId(null);
        return reservaHabitacionRepository.save(detalle);
    }

    public ReservaHabitacion actualizar(Long id, ReservaHabitacion datos) {
        ReservaHabitacion existente = obtener(id);
        existente.setReserva(resolverReserva(datos.getReservaId()));
        existente.setHabitacion(resolverHabitacion(datos.getHabitacionId()));
        return reservaHabitacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!reservaHabitacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("ReservaHabitacion", id);
        }
        reservaHabitacionRepository.deleteById(id);
    }

    private Reserva resolverReserva(Long reservaId) {
        if (reservaId == null) {
            throw new ReglaNegocioException("El detalle debe tener un reservaId.");
        }
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));
    }

    private Habitacion resolverHabitacion(Long habitacionId) {
        if (habitacionId == null) {
            throw new ReglaNegocioException("El detalle debe tener un habitacionId.");
        }
        return habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", habitacionId));
    }
}
