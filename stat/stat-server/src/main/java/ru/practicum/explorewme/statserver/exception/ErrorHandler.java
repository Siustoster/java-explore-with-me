package ru.practicum.explorewme.statserver.exception;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice("ru.practicum.explorewme.statserver")
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> handlePreValidationExceptions(MethodArgumentNotValidException exception) {
        List<ErrorResponse> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            ErrorResponse errorResponse = new ErrorResponse(((FieldError) error).getField(), error.getDefaultMessage());
            errors.add(errorResponse);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleIternalServerError(Exception exception) {
        return Map.of("error", exception.getMessage());
    }
}