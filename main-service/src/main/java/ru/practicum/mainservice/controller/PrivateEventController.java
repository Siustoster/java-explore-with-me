package ru.practicum.mainservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.model.event.dto.NewEventDto;
import ru.practicum.mainservice.model.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.model.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.model.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.model.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.service.ParticipationRequestService;


import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable @Positive Integer userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос от private контроллера на получение списка событий, добавленных пользователем с Id={}", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Integer userId,
                                  @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос от private контроллера на публикацию нового события от пользователя с Id={}", userId);
        return eventService.save(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение события с Id={}, добавленного пользователем с Id={}", eventId, userId);
        return eventService.getEventOfUserForPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventOfUser(@PathVariable Integer userId,
                                          @PathVariable Integer eventId,
                                          @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Запрос от private контроллера от пользователя с id={} на обновление события с id={}", userId, eventId);
        return eventService.updateByUser(userId, eventId, updateEventRequest);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение информации о запросах на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return participationRequestService.getRequestsForParticipationInUserEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Запрос от private контроллера на изменение статуса запросов на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return participationRequestService.updateRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
