package com.acm.proyectohotel.controlador;

import com.acm.proyectohotel.entidad.CadenaHotel;
import com.acm.proyectohotel.servicio.CadenaHotelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cadenas-hotel")
public class CadenaHotelController {

    private final CadenaHotelService cadenaHotelService;

    public CadenaHotelController(CadenaHotelService cadenaHotelService) {
        this.cadenaHotelService = cadenaHotelService;
    }

    @GetMapping
    public List<CadenaHotel> listar() {
        return cadenaHotelService.listar();
    }

    @GetMapping("/{id}")
    public CadenaHotel obtener(@PathVariable Long id) {
        return cadenaHotelService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CadenaHotel crear(@RequestBody CadenaHotel cadena) {
        return cadenaHotelService.crear(cadena);
    }

    @PutMapping("/{id}")
    public CadenaHotel actualizar(@PathVariable Long id, @RequestBody CadenaHotel cadena) {
        return cadenaHotelService.actualizar(id, cadena);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cadenaHotelService.eliminar(id);
    }
}
