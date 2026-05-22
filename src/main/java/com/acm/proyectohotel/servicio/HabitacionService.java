package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Habitacion;
import com.acm.proyectohotel.entidad.Sucursal;
import com.acm.proyectohotel.entidad.TipoHabitacion;
import com.acm.proyectohotel.enums.EstadoHabitacion;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.HabitacionRepository;
import com.acm.proyectohotel.repositorio.SucursalRepository;
import com.acm.proyectohotel.repositorio.TipoHabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final SucursalRepository sucursalRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository,
                             SucursalRepository sucursalRepository,
                             TipoHabitacionRepository tipoHabitacionRepository) {
        this.habitacionRepository = habitacionRepository;
        this.sucursalRepository = sucursalRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Habitacion> listar() {
        return habitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Habitacion obtener(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", id));
    }

    public Habitacion crear(Habitacion habitacion) {
        Sucursal sucursal = resolverSucursal(habitacion.getSucursalId());
        habitacion.setSucursal(sucursal);
        habitacion.setTipo(resolverTipo(habitacion.getTipoId()));
        if (habitacionRepository.existsBySucursal_IdAndNumero(sucursal.getId(), habitacion.getNumero())) {
            throw new ReglaNegocioException(
                    "La sucursal ya tiene una habitacion con el numero '" + habitacion.getNumero() + "'.");
        }
        aplicarValoresPorDefecto(habitacion);
        habitacion.setId(null);
        return habitacionRepository.save(habitacion);
    }

    public Habitacion actualizar(Long id, Habitacion datos) {
        Habitacion existente = obtener(id);
        Sucursal sucursal = resolverSucursal(datos.getSucursalId());
        boolean cambioNumeroOSucursal = !existente.getSucursal().getId().equals(sucursal.getId())
                || !existente.getNumero().equals(datos.getNumero());
        if (cambioNumeroOSucursal
                && habitacionRepository.existsBySucursal_IdAndNumero(sucursal.getId(), datos.getNumero())) {
            throw new ReglaNegocioException(
                    "La sucursal ya tiene una habitacion con el numero '" + datos.getNumero() + "'.");
        }
        existente.setSucursal(sucursal);
        existente.setTipo(resolverTipo(datos.getTipoId()));
        existente.setNumero(datos.getNumero());
        existente.setPiso(datos.getPiso());
        existente.setCapacidad(datos.getCapacidad());
        existente.setPrecioNoche(datos.getPrecioNoche());
        existente.setEstado(datos.getEstado() != null ? datos.getEstado() : existente.getEstado());
        existente.setDisponible(datos.getDisponible() != null ? datos.getDisponible() : existente.getDisponible());
        return habitacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!habitacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Habitacion", id);
        }
        habitacionRepository.deleteById(id);
    }

    private void aplicarValoresPorDefecto(Habitacion habitacion) {
        if (habitacion.getEstado() == null) {
            habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
        }
        if (habitacion.getDisponible() == null) {
            habitacion.setDisponible(habitacion.getEstado() == EstadoHabitacion.DISPONIBLE);
        }
    }

    private Sucursal resolverSucursal(Long sucursalId) {
        if (sucursalId == null) {
            throw new ReglaNegocioException("La habitacion debe tener un sucursalId.");
        }
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal", sucursalId));
    }

    private TipoHabitacion resolverTipo(Long tipoId) {
        if (tipoId == null) {
            throw new ReglaNegocioException("La habitacion debe tener un tipoId.");
        }
        return tipoHabitacionRepository.findById(tipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", tipoId));
    }
}
