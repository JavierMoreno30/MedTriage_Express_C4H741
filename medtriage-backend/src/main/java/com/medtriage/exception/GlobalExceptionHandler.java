package com.medtriage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), "recurso-no-encontrado");
    }

    @ExceptionHandler(DoctorNoDisponibleException.class)
    public ProblemDetail doctorNoDisponible(DoctorNoDisponibleException ex) {
        return problema(HttpStatus.CONFLICT, "Doctor no disponible", ex.getMessage(), "doctor-no-disponible");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail argumentoInvalido(IllegalArgumentException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage(), "solicitud-invalida");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problema(HttpStatus.BAD_REQUEST, "Error de validación", detalle, "error-validacion");
    }

    private ProblemDetail problema(HttpStatus status, String titulo, String detalle, String tipo) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detalle);
        pd.setTitle(titulo);
        pd.setType(URI.create("https://medtriage.com/errors/" + tipo));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
