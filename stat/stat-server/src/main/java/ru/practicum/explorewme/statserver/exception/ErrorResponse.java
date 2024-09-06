package ru.practicum.explorewme.statserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String fieldName;
    private String errorMessage;
}
