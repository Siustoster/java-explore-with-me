package ru.practicum.explorewme.statserver.exception;

public class BadRequestValidationException extends RuntimeException {
    public BadRequestValidationException(final String message) {
        super(message);
    }
}
