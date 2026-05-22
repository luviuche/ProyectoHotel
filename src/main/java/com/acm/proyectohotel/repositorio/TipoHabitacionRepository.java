package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {

    boolean existsByNombre(String nombre);
}
