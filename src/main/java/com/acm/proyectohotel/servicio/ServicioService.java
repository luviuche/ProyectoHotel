package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Servicio;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.repositorio.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Servicio obtener(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio", id));
    }

    public Servicio crear(Servicio servicio) {
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }
        servicio.setId(null);
        return servicioRepository.save(servicio);
    }

    public Servicio actualizar(Long id, Servicio datos) {
        Servicio existente = obtener(id);
        existente.setNombre(datos.getNombre());
        existente.setTipo(datos.getTipo());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setActivo(datos.getActivo() != null ? datos.getActivo() : existente.getActivo());
        return servicioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Servicio", id);
        }
        servicioRepository.deleteById(id);
    }
}
