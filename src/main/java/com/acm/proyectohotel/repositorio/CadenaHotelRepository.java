package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.CadenaHotel;
import com.acm.proyectohotel.enums.EstadoGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadenaHotelRepository extends JpaRepository<CadenaHotel, Long> {

    List<CadenaHotel> findByEstado(EstadoGeneral estado);
}
