package gt.edu.umg.desarrolloweb.consulta.controller;

import gt.edu.umg.desarrolloweb.consulta.api.dto.ErrorDto;
import gt.edu.umg.desarrolloweb.consulta.service.ContadorNoEncontradoException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * Convierte las excepciones al esquema "Error" del contrato.
 * Seguridad: el cliente NUNCA recibe un stack trace; los errores inesperados se registran
 * en el servidor y el cliente solo ve un mensaje generico.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ContadorNoEncontradoException.class)
    public ResponseEntity<ErrorDto> handleNotFound(ContadorNoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    /** El codigo del path no cumple el contrato (longitud o caracteres). */
    @ExceptionHandler({HandlerMethodValidationException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorDto> handleParameterValidation(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Código de contador inválido", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnexpected(Exception ex) {
        // Las excepciones web de Spring (405, 404 de ruta, 415...) ya traen su estado: se conserva.
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatus status = HttpStatus.valueOf(errorResponse.getStatusCode().value());
            return build(status, status.getReasonPhrase(), List.of());
        }
        log.error("Error inesperado", ex); // el detalle completo queda solo en el log del servidor
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", List.of());
    }

    private ResponseEntity<ErrorDto> build(HttpStatus status, String message, List<String> details) {
        ErrorDto body = new ErrorDto(status.value(), message).details(details);
        return ResponseEntity.status(status).body(body);
    }
}
