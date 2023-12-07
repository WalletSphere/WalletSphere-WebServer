package com.khomishchak.ws.controllers;

import com.khomishchak.ws.exceptions.BalanceNotFoundException;
import com.khomishchak.ws.services.integration.whitebit.exceptions.WhiteBitClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AbstractRestController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        result.put("errors", errors);
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(BalanceNotFoundException.class)
    public ResponseEntity<?> handleBalanceNotFoundException(BalanceNotFoundException ex) {
        Map<String, String> result = new HashMap<>();
        result.put(ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(WhiteBitClientException.class)
    public ResponseEntity<?> handleBalanceNotFoundException(WhiteBitClientException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ex.getCode());
        result.put("message", ex.getMessage());
        result.put("errors", ex.getErrors());
        return ResponseEntity.badRequest().body(result);
    }
}
