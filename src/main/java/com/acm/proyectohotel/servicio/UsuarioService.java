package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.Rol;
import com.acm.proyectohotel.entidad.Usuario;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.RolRepository;
import com.acm.proyectohotel.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ReglaNegocioException("Ya existe un usuario con el email '" + usuario.getEmail() + "'.");
        }
        if (usuarioRepository.existsByDocumento(usuario.getDocumento())) {
            throw new ReglaNegocioException("Ya existe un usuario con el documento '" + usuario.getDocumento() + "'.");
        }
        usuario.setRol(resolverRol(usuario.getRolId()));
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = obtener(id);
        if (!existente.getEmail().equals(datos.getEmail())
                && usuarioRepository.existsByEmail(datos.getEmail())) {
            throw new ReglaNegocioException("Ya existe un usuario con el email '" + datos.getEmail() + "'.");
        }
        if (!existente.getDocumento().equals(datos.getDocumento())
                && usuarioRepository.existsByDocumento(datos.getDocumento())) {
            throw new ReglaNegocioException("Ya existe un usuario con el documento '" + datos.getDocumento() + "'.");
        }
        existente.setRol(resolverRol(datos.getRolId()));
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDocumento(datos.getDocumento());
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            existente.setPassword(datos.getPassword());
        }
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
    }

    private Rol resolverRol(Long rolId) {
        if (rolId == null) {
            throw new ReglaNegocioException("El usuario debe tener un rolId.");
        }
        return rolRepository.findById(rolId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", rolId));
    }
}
