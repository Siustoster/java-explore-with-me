package ru.practicum.explorewme.statserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;
import ru.practicum.explorewme.statserver.service.StatService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ServerController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody HitRequestDto hitDto) {
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStat(@RequestParam String start,
                           @RequestParam String end,
                           @RequestParam(required = false) List<String> uri,
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
        return service.getStat(startDT, endDT, unique, uri);
    }

}
