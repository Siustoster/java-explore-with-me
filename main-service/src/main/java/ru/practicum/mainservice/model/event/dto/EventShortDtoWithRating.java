package ru.practicum.mainservice.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.category.dto.CategoryDto;
import ru.practicum.mainservice.model.user.dto.UserShortDtoWithRating;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDtoWithRating {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String eventDate;
    protected UserShortDtoWithRating initiator;
    protected Boolean paid;
    protected String title;
    protected int views;
    protected int rating;
}
