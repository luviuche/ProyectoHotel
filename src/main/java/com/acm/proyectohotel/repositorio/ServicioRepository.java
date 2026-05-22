package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.Servicio;
import com.acm.proyectohotel.enums.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrue();

    List<Servicio> findByTipo(TipoServicio tipo);
}
