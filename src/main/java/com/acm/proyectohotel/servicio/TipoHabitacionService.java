package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.TipoHabitacion;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.TipoHabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TipoHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;

    public TipoHabitacionService(TipoHabitacionRepository tipoHabitacionRepository) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoHabitacion> listar() {
        return tipoHabitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoHabitacion obtener(Long id) {
        return tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", id));
    }

    public TipoHabitacion crear(TipoHabitacion tipo) {
        if (tipoHabitacionRepository.existsByNombre(tipo.getNombre())) {
            throw new ReglaNegocioException("Ya existe un tipo de habitacion con el nombre '" + tipo.getNombre() + "'.");
        }
        tipo.setId(null);
        return tipoHabitacionRepository.save(tipo);
    }

    public TipoHabitacion actualizar(Long id, TipoHabitacion datos) {
        TipoHabitacion existente = obtener(id);
        if (!existente.getNombre().equals(datos.getNombre())
                && tipoHabitacionRepository.existsByNombre(datos.getNombre())) {
            throw new ReglaNegocioException("Ya existe un tipo de habitacion con el nombre '" + datos.getNombre() + "'.");
        }
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return tipoHabitacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!tipoHabitacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("TipoHabitacion", id);
        }
        tipoHabitacionRepository.deleteById(id);
    }
}
