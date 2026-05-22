package com.acm.proyectohotel.excepcion;

/**
 * Se lanza cuando se solicita un recurso por id que no existe en la base de datos.
 * El manejador global la traduce a una respuesta HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String entidad, Long id) {
        super(entidad + " con id " + id + " no encontrado.");
    }
}
