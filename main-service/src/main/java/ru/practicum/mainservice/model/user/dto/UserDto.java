package ru.practicum.mainservice.model.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    protected Integer id;
    @NotBlank
    @Size(min = 2, max = 250)
    protected String name;
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    protected String email;
}
