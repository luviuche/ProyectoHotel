package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.CadenaHotel;
import com.acm.proyectohotel.enums.EstadoGeneral;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.repositorio.CadenaHotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CadenaHotelService {

    private final CadenaHotelRepository cadenaHotelRepository;

    public CadenaHotelService(CadenaHotelRepository cadenaHotelRepository) {
        this.cadenaHotelRepository = cadenaHotelRepository;
    }

    @Transactional(readOnly = true)
    public List<CadenaHotel> listar() {
        return cadenaHotelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CadenaHotel obtener(Long id) {
        return cadenaHotelRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("CadenaHotel", id));
    }

    public CadenaHotel crear(CadenaHotel cadena) {
        if (cadena.getEstado() == null) {
            cadena.setEstado(EstadoGeneral.ACTIVO);
        }
        cadena.setId(null);
        return cadenaHotelRepository.save(cadena);
    }

    public CadenaHotel actualizar(Long id, CadenaHotel datos) {
        CadenaHotel existente = obtener(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setEstado(datos.getEstado() != null ? datos.getEstado() : existente.getEstado());
        return cadenaHotelRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!cadenaHotelRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("CadenaHotel", id);
        }
        cadenaHotelRepository.deleteById(id);
    }
}
