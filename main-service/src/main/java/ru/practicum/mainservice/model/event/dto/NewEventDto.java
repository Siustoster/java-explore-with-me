package ru.practicum.mainservice.model.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewEventDto {
    protected Integer id;
    @NotBlank
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    protected String description;
    @NotBlank
    protected String eventDate;
    @NotNull
    protected LocationDto location;
    protected Boolean paid;
    @PositiveOrZero
    protected int participantLimit;
    protected Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    protected String title;
}
