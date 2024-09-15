package ru.practicum.mainservice.model.event.dto;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @Size(min = 20, max = 7000)
    protected String description;
    protected String eventDate;
    protected LocationDto location;
    protected Boolean paid;
    @PositiveOrZero
    protected Integer participantLimit;
    protected Boolean requestModeration;
    protected String stateAction;
    @Size(min = 3, max = 120)
    protected String title;
}
