package ru.practicum.mainservice.mappers;

import ru.practicum.mainservice.model.Constants;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.event.Location;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.model.event.dto.NewEventDto;
import ru.practicum.mainservice.model.user.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location) {
        return new Event(
                newEventDto.getId() != null ? newEventDto.getId() : 0,
                newEventDto.getAnnotation() != null ? newEventDto.getAnnotation() : "",
                category,
                LocalDateTime.now(),
                newEventDto.getDescription() != null ? newEventDto.getDescription() : "",
                newEventDto.getEventDate() != null ? LocalDateTime.parse(newEventDto.getEventDate(), Constants.DATE_TIME_FORMAT) : null,
                initiator,
                location,
                newEventDto.getPaid() != null ? newEventDto.getPaid() : false,
                newEventDto.getParticipantLimit() != 0 ? newEventDto.getParticipantLimit() : 0,
                0,
                LocalDateTime.now(),
                newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true,
                EventState.PENDING,
                newEventDto.getTitle() != null ? newEventDto.getTitle() : ""
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(Constants.DATE_TIME_FORMAT),
                event.getDescription(),
                event.getEventDate().format(Constants.DATE_TIME_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn().format(Constants.DATE_TIME_FORMAT),
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                0
        );
    }

    public static EventShortDto fromFullToShortEventDTO(EventFullDto eventFullDto) {
        return new EventShortDto(
                eventFullDto.getId(),
                eventFullDto.getAnnotation(),
                eventFullDto.getCategory(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getEventDate(),
                eventFullDto.getInitiator(),
                eventFullDto.getPaid(),
                eventFullDto.getTitle(),
                eventFullDto.getViews()
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(Constants.DATE_TIME_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0
        );
    }
}
