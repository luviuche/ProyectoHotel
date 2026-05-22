package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Rol;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rol obtener(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", id));
    }

    public Rol crear(Rol rol) {
        if (rolRepository.existsByNombre(rol.getNombre())) {
            throw new ReglaNegocioException("Ya existe un rol con el nombre '" + rol.getNombre() + "'.");
        }
        rol.setId(null);
        return rolRepository.save(rol);
    }

    public Rol actualizar(Long id, Rol datos) {
        Rol existente = obtener(id);
        if (!existente.getNombre().equals(datos.getNombre())
                && rolRepository.existsByNombre(datos.getNombre())) {
            throw new ReglaNegocioException("Ya existe un rol con el nombre '" + datos.getNombre() + "'.");
        }
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return rolRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Rol", id);
        }
        rolRepository.deleteById(id);
    }
}
