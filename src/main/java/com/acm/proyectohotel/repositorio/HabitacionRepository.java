package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Habitacion;
import com.acm.proyectohotel.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // Guion bajo: fuerza la travesia sucursal.id (evita colision con getSucursalId()).
    List<Habitacion> findBySucursal_Id(Long sucursalId);

    List<Habitacion> findByEstado(EstadoHabitacion estado);

    List<Habitacion> findByDisponibleTrue();

    boolean existsBySucursal_IdAndNumero(Long sucursalId, String numero);
}
