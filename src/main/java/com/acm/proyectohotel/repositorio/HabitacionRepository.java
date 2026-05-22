package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Habitacion;
import com.acm.proyectohotel.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findBySucursalId(Long sucursalId);

    List<Habitacion> findByEstado(EstadoHabitacion estado);

    List<Habitacion> findByDisponibleTrue();

    boolean existsBySucursalIdAndNumero(Long sucursalId, String numero);
}
