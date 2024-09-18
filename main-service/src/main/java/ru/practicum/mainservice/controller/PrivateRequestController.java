package ru.practicum.mainservice.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.service.ParticipationRequestService;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Integer userId) {
        log.info("Запрос от private контроллера на получение запросов пользователя с id={} на участие в чужих событиях ", userId);
        return participationRequestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable @Positive Integer userId,
                                        @RequestParam("eventId") @Positive Integer eventId) {
        log.info("Запрос от private контроллера на создание запроса на участие в событии: userId = {}, eventId = {}", userId, eventId);
        return participationRequestService.save(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer requestId) {
        log.info("Запрос от private контроллера на отмену запроса с Id = {} пользователя с Id = {}", requestId, userId);
        return participationRequestService.cancel(userId, requestId);
    }
}
