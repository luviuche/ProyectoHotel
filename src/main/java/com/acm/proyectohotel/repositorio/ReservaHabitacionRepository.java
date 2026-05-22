package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {

    List<ReservaHabitacion> findByReservaId(Long reservaId);

    List<ReservaHabitacion> findByHabitacionId(Long habitacionId);
}
