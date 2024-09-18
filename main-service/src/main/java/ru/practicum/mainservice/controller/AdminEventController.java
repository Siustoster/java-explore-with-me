package ru.practicum.mainservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.searchparams.PresentationParameters;
import ru.practicum.mainservice.searchparams.SearchParametersAdmin;
import ru.practicum.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventFullDto> getEventsWithFilteringForAdmin(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        SearchParametersAdmin searchParametersAdmin = new SearchParametersAdmin(users, states, categories, rangeStart, rangeEnd);
        PresentationParameters presentationParameters = new PresentationParameters(null, from, size);
        log.info("Контроллер админа получил запрос на вывод списка событий с фильтрацией");
        return eventService.getEventsWithFilteringForAdmin(searchParametersAdmin, presentationParameters);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Контроллер админа получил запрос на обновление Event id = {}", eventId);
        return eventService.updateByAdmin(eventId, updateEventRequest);
    }
}
