package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDocumento(String documento);

    // Guion bajo: fuerza la travesia rol.id (evita colision con el getter getRolId()).
    List<Usuario> findByRol_Id(Long rolId);
}
