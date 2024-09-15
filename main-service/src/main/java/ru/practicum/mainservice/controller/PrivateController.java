package ru.practicum.mainservice.controller;

import lombok.AllArgsConstructor;
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
import ru.practicum.mainservice.service.PrivateService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@AllArgsConstructor
@Slf4j
public class PrivateController {
    private final PrivateService privateService;

    /**
     * События
     */

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable @Positive Integer userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос от private контроллера на получение списка событий, добавленных пользователем с Id={}", userId);
        return privateService.getUserEvents(userId, from, size);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Integer userId,
                                  @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос от private контроллера на публикацию нового события от пользователя с Id={}", userId);
        return privateService.saveEvent(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение события с Id={}, добавленного пользователем с Id={}", eventId, userId);
        return privateService.getEventOfUser(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventOfUser(@PathVariable Integer userId,
                                          @PathVariable Integer eventId,
                                          @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Запрос от private контроллера от пользователя с id={} на обновление события с id={}", userId, eventId);
        return privateService.updateEventOfUser(userId, eventId, updateEventRequest);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение информации о запросах на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return privateService.getRequestsForParticipationInUserEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Запрос от private контроллера на изменение статуса запросов на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return privateService.updateRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    /**
     * Запросы на участие
     */

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Integer userId) {
        log.info("Запрос от private контроллера на получение запросов пользователя с id={} на участие в чужих событиях ", userId);
        return privateService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable @Positive Integer userId,
                                        @RequestParam("eventId") @Positive Integer eventId) {
        log.info("Запрос от private контроллера на создание запроса на участие в событии: userId = {}, eventId = {}", userId, eventId);
        return privateService.saveParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer requestId) {
        log.info("Запрос от private контроллера на отмену запроса с Id = {} пользователя с Id = {}", requestId, userId);
        return privateService.cancelParticipationRequest(userId, requestId);
    }
}
