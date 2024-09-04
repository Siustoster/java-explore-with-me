package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.client.StatClient;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EventController {
    private final StatClient statClient;

    private String appName = "EWM";
    @Value("${stats.server.url}")
    private String url;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public void getEvents(ServerHttpRequest request) {
        String statUrl = url;
        //StatsClient statsClient = new StatClientImpl(statUrl);
        log.info("GET /events");
        log.info(url);
        String resp = statClient.saveStat(request, appName);
        log.info(resp);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void getEventById(ServerHttpRequest request, @PathVariable Long id) {
        log.info("GET /eventsP{}", id);
        //statsClient.saveStat(request, appName);
    }
}
