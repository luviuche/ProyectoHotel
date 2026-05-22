package com.acm.proyectohotel.controlador;

import com.acm.proyectohotel.entidad.TipoHabitacion;
import com.acm.proyectohotel.servicio.TipoHabitacionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-habitacion")
public class TipoHabitacionController {

    private final TipoHabitacionService tipoHabitacionService;

    public TipoHabitacionController(TipoHabitacionService tipoHabitacionService) {
        this.tipoHabitacionService = tipoHabitacionService;
    }

    @GetMapping
    public List<TipoHabitacion> listar() {
        return tipoHabitacionService.listar();
    }

    @GetMapping("/{id}")
    public TipoHabitacion obtener(@PathVariable Long id) {
        return tipoHabitacionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoHabitacion crear(@RequestBody TipoHabitacion tipo) {
        return tipoHabitacionService.crear(tipo);
    }

    @PutMapping("/{id}")
    public TipoHabitacion actualizar(@PathVariable Long id, @RequestBody TipoHabitacion tipo) {
        return tipoHabitacionService.actualizar(id, tipo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
    }
}
