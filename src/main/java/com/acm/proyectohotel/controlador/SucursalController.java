package com.acm.proyectohotel.controlador;

import com.acm.proyectohotel.entidad.Sucursal;
import com.acm.proyectohotel.servicio.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    public List<Sucursal> listar() {
        return sucursalService.listar();
    }

    @GetMapping("/{id}")
    public Sucursal obtener(@PathVariable Long id) {
        return sucursalService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sucursal crear(@RequestBody Sucursal sucursal) {
        return sucursalService.crear(sucursal);
    }

    @PutMapping("/{id}")
    public Sucursal actualizar(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        return sucursalService.actualizar(id, sucursal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        sucursalService.eliminar(id);
    }
}
