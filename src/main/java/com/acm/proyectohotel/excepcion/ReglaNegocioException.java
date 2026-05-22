package com.acm.proyectohotel.excepcion;

/**
 * Se lanza cuando una operacion viola una regla de negocio o de validacion
 * (por ejemplo, un email duplicado o fechas incoherentes).
 * El manejador global la traduce a una respuesta HTTP 400.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
