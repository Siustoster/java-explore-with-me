package ru.practicum.explorewme.statserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;

@RestController
public class ServerController {
    @PostMapping("/hit")
    public void addHit(@RequestBody HitRequestDto hitDto) {

    }

    @GetMapping("/stats")
    public StatDto getStat() {
        return null;
    }

}
