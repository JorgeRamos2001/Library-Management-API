package com.app.exceptions;

import com.app.models.responses.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .exceptionType("INTERNAL_SERVER_ERROR")
                .details("An unexpected error occurred.")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex,  WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .exceptionType("NOT_FOUND")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateResourceException(DuplicateResourceException ex,  WebRequest request) {
        log.warn("Duplicate resource conflict: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .exceptionType("CONFLICT")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotPermittedException(OperationNotPermittedException ex,  WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value())
                .exceptionType("OPERATION_NOT_PERMITTED")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex,  WebRequest request) {
        log.warn("Bad Request: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .exceptionType("BAD_REQUEST")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,  WebRequest request) {
        log.warn("Bad Request: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .exceptionType("BAD_REQUEST")
                .details(errors)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST);
    }
}
