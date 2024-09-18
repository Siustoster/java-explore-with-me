package ru.practicum.mainservice.exception;

public class ConflictValidationException extends RuntimeException {
    public ConflictValidationException(final String message) {
        super(message);
    }
}
