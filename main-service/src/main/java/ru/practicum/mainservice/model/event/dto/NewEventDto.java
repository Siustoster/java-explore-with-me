package ru.practicum.mainservice.model.event.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewEventDto {
    private Integer id;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
