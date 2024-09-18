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
    private Integer id;
    @NotBlank
    @Size(min = 5)
    private String name;
}
