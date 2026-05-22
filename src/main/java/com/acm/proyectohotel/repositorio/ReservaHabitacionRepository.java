package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {

    // Guion bajo: fuerza la travesia reserva.id / habitacion.id (evita colision con los getters).
    List<ReservaHabitacion> findByReserva_Id(Long reservaId);

    List<ReservaHabitacion> findByHabitacion_Id(Long habitacionId);
}
