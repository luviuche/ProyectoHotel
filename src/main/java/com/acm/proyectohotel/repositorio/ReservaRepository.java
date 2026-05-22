package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Reserva;
import com.acm.proyectohotel.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByEstado(EstadoReserva estado);
}
