package ru.practicum.mainservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.enums.SortType;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.searchparams.PresentationParameters;
import ru.practicum.mainservice.searchparams.SearchParametersUsersPublic;
import ru.practicum.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEventsWithFiltering(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Integer> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false) SortType sort,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size,
                                                      HttpServletRequest servletRequest) {

        SearchParametersUsersPublic searchParametersUsersPublic = new SearchParametersUsersPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        PresentationParameters presentationParameters = new PresentationParameters(sort, from, size);
        return eventService.getEventsWithFilteringForPublic(searchParametersUsersPublic, presentationParameters, servletRequest);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventForPublic(@PathVariable @Positive Integer id,
                                          HttpServletRequest servletRequest) {

        return eventService.getEventForPublic(id, servletRequest);
    }
}
