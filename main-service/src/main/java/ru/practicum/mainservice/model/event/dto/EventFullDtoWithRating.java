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
public class EventFullDtoWithRating {
    private Integer id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDtoWithRating initiator;
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private int views;
    private int rating;
}