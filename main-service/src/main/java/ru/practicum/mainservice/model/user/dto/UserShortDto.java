package ru.practicum.mainservice.model.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
