package com.acm.proyectohotel.excepcion;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centraliza el manejo de errores de toda la API y devuelve respuestas JSON
 * uniformes con el codigo HTTP adecuado.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        HttpStatus estado = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(estado)
                .body(RespuestaError.de(estado.value(), estado.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<RespuestaError> manejarReglaNegocio(ReglaNegocioException ex) {
        HttpStatus estado = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(estado)
                .body(RespuestaError.de(estado.value(), estado.getReasonPhrase(), ex.getMessage()));
    }

    /** Restricciones de la base de datos: unicidad, claves foraneas, checks, etc. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> manejarIntegridad(DataIntegrityViolationException ex) {
        HttpStatus estado = HttpStatus.CONFLICT;
        String mensaje = "La operacion viola una restriccion de integridad de los datos.";
        return ResponseEntity.status(estado)
                .body(RespuestaError.de(estado.value(), estado.getReasonPhrase(), mensaje));
    }
}
