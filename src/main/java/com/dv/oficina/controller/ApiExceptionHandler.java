package com.dv.oficina.controller;

import com.dv.oficina.dto.ErroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> status(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new ErroResponse(e.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        String campos = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .distinct()
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new ErroResponse("Dados inválidos nos campos: " + campos));
    }
}