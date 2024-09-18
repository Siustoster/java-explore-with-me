package ru.practicum.explorewme.statserver.exception;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestValidationException.class)
    public List<ErrorResponse> handlePreValidationExceptions(BadRequestValidationException exception) {
        List<ErrorResponse> errors = new ArrayList<>();

        errors.add(new ErrorResponse(exception.getMessage(), "error"));

        return errors;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final MissingServletRequestParameterException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerError(Exception exception) {
        return new ErrorResponse("Error", exception.getMessage());
    }
}