package com.acm.proyectohotel.controlador;

import com.acm.proyectohotel.entidad.ReservaHabitacion;
import com.acm.proyectohotel.servicio.ReservaHabitacionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva-habitaciones")
public class ReservaHabitacionController {

    private final ReservaHabitacionService reservaHabitacionService;

    public ReservaHabitacionController(ReservaHabitacionService reservaHabitacionService) {
        this.reservaHabitacionService = reservaHabitacionService;
    }

    @GetMapping
    public List<ReservaHabitacion> listar() {
        return reservaHabitacionService.listar();
    }

    @GetMapping("/{id}")
    public ReservaHabitacion obtener(@PathVariable Long id) {
        return reservaHabitacionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaHabitacion crear(@RequestBody ReservaHabitacion detalle) {
        return reservaHabitacionService.crear(detalle);
    }

    @PutMapping("/{id}")
    public ReservaHabitacion actualizar(@PathVariable Long id, @RequestBody ReservaHabitacion detalle) {
        return reservaHabitacionService.actualizar(id, detalle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        reservaHabitacionService.eliminar(id);
    }
}
