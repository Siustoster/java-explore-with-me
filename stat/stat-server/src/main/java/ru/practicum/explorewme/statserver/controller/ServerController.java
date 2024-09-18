package ru.practicum.explorewme.statserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;
import ru.practicum.explorewme.statserver.exception.BadRequestValidationException;
import ru.practicum.explorewme.statserver.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ServerController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody HitRequestDto hitDto) {
        log.info("created hit");
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("get stats");
        if (start.isAfter(LocalDateTime.now()))
            throw new BadRequestValidationException("Дата начала не может быть в будущем");
        if (end.isBefore(LocalDateTime.now()))
            throw new BadRequestValidationException("Дата конца не может быть в прошлом");
        return service.getStat(start, end, unique, uris);
    }

}
