package com.acm.proyectohotel.controlador;

import com.acm.proyectohotel.entidad.ReservaServicio;
import com.acm.proyectohotel.servicio.ReservaServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva-servicios")
public class ReservaServicioController {

    private final ReservaServicioService reservaServicioService;

    public ReservaServicioController(ReservaServicioService reservaServicioService) {
        this.reservaServicioService = reservaServicioService;
    }

    @GetMapping
    public List<ReservaServicio> listar() {
        return reservaServicioService.listar();
    }

    @GetMapping("/{id}")
    public ReservaServicio obtener(@PathVariable Long id) {
        return reservaServicioService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaServicio crear(@RequestBody ReservaServicio detalle) {
        return reservaServicioService.crear(detalle);
    }

    @PutMapping("/{id}")
    public ReservaServicio actualizar(@PathVariable Long id, @RequestBody ReservaServicio detalle) {
        return reservaServicioService.actualizar(id, detalle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        reservaServicioService.eliminar(id);
    }
}
