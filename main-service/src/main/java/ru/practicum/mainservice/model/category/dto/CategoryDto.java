package ru.practicum.mainservice.model.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    protected Integer id;
    @NotBlank
    @Size(min = 1, max = 50)
    @NotBlank
    protected String name;
}