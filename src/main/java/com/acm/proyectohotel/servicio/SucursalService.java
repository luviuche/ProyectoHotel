package com.acm.proyectohotel.servicio;

import com.acm.proyectohotel.entidad.CadenaHotel;
import com.acm.proyectohotel.entidad.Sucursal;
import com.acm.proyectohotel.enums.EstadoGeneral;
import com.acm.proyectohotel.excepcion.RecursoNoEncontradoException;
import com.acm.proyectohotel.excepcion.ReglaNegocioException;
import com.acm.proyectohotel.repositorio.CadenaHotelRepository;
import com.acm.proyectohotel.repositorio.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final CadenaHotelRepository cadenaHotelRepository;

    public SucursalService(SucursalRepository sucursalRepository, CadenaHotelRepository cadenaHotelRepository) {
        this.sucursalRepository = sucursalRepository;
        this.cadenaHotelRepository = cadenaHotelRepository;
    }

    @Transactional(readOnly = true)
    public List<Sucursal> listar() {
        return sucursalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sucursal obtener(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal", id));
    }

    public Sucursal crear(Sucursal sucursal) {
        sucursal.setCadenaHotel(resolverCadena(sucursal.getCadenaId()));
        if (sucursal.getEstado() == null) {
            sucursal.setEstado(EstadoGeneral.ACTIVO);
        }
        sucursal.setId(null);
        return sucursalRepository.save(sucursal);
    }

    public Sucursal actualizar(Long id, Sucursal datos) {
        Sucursal existente = obtener(id);
        existente.setCadenaHotel(resolverCadena(datos.getCadenaId()));
        existente.setNombre(datos.getNombre());
        existente.setDireccion(datos.getDireccion());
        existente.setCiudad(datos.getCiudad());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        existente.setCategoria(datos.getCategoria());
        existente.setEstado(datos.getEstado() != null ? datos.getEstado() : existente.getEstado());
        return sucursalRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!sucursalRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Sucursal", id);
        }
        sucursalRepository.deleteById(id);
    }

    private CadenaHotel resolverCadena(Long cadenaId) {
        if (cadenaId == null) {
            throw new ReglaNegocioException("La sucursal debe tener un cadenaId.");
        }
        return cadenaHotelRepository.findById(cadenaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("CadenaHotel", cadenaId));
    }
}
