package com.expertofordering.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<ApiError.FieldError> fields = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                req.getRequestURI(),
                fields
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleRse(ResponseStatusException e, HttpServletRequest req) {
        HttpStatusCode code = e.getStatusCode();
        ApiError body = new ApiError(
                Instant.now(),
                code.value(),
                code.toString(),
                e.getReason(),
                req.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(code).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleEtc(Exception e, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                500,
                "Internal Server Error",
                "Unexpected error",
                req.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(500).body(body);
    }
}
