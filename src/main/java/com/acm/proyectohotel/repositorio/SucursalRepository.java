package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByCadenaHotelId(Long cadenaId);

    List<Sucursal> findByCiudad(String ciudad);
}
