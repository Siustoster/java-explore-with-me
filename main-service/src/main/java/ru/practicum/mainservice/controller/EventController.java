package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.client.StatClient;
import ru.practicum.explorewme.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EventController {
    private final StatClient statClient;
    //ЗДЕСЬ ВСЁ ТОЛЬКО ДЛЯ ТЕСТА КЛИЕНТА
    private String appName = "EWM";
    @Value("${stats.server.url}")
    private String url;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public void getEvents(ServerHttpRequest request) {
        String statUrl = url;
        log.info("GET /events");
        log.info(url);
        statClient.saveStat(request, appName);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void getEventById(ServerHttpRequest request, @PathVariable Long id) {
        log.info("GET /eventsP{}", id);
    }

    @GetMapping("/stat")
    public List<StatDto> getStat(@RequestParam String start,
                           @RequestParam String end,
                           @RequestParam(required = false) List<String> uris,
                           @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startDT;
        LocalDateTime endDT;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startDT = LocalDateTime.parse(start, formatter);
            endDT = LocalDateTime.parse(end, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Дата должна быть в формате yyyy-MM-dd HH:mm:ss");
        }
        return statClient.getStat(start, end, uris, unique);
    }
}
