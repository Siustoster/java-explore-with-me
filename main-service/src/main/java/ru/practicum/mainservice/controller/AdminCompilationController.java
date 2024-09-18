package ru.practicum.mainservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.compilation.dto.CompilationDto;
import ru.practicum.mainservice.model.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.service.CompilationService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Контроллер админа получил запрос на добавление новой подборки событий");
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Integer compId) {
        log.info("Контроллер админа получил запрос на удаление подборки с id = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Integer compId,
                                            @RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Контроллер админа получил запрос на обновление подборки событий");
        return compilationService.updateCompilation(compId, newCompilationDto);
    }
}
