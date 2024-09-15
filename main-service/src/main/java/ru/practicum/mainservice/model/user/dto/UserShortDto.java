package ru.practicum.mainservice.model.user.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserShortDto {
    protected Integer id;
    @NotBlank
    @Size(min = 5)
    protected String name;
}
