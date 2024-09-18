package ru.practicum.mainservice.exception;

public class BadRequestValidationException extends RuntimeException {
    public BadRequestValidationException(final String message) {
        super(message);
    }
}
