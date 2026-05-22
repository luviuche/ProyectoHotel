package com.acm.proyectohotel.excepcion;

import java.time.LocalDateTime;

/**
 * Cuerpo JSON uniforme para las respuestas de error de la API.
 */
public record RespuestaError(
        int estado,
        String error,
        String mensaje,
        LocalDateTime fecha
) {
    public static RespuestaError de(int estado, String error, String mensaje) {
        return new RespuestaError(estado, error, mensaje, LocalDateTime.now());
    }
}
