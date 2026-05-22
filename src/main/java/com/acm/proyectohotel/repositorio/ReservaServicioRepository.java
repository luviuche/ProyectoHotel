package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.ReservaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaServicioRepository extends JpaRepository<ReservaServicio, Long> {

    // Guion bajo: fuerza la travesia reserva.id / servicio.id (evita colision con los getters).
    List<ReservaServicio> findByReserva_Id(Long reservaId);

    List<ReservaServicio> findByServicio_Id(Long servicioId);
}
