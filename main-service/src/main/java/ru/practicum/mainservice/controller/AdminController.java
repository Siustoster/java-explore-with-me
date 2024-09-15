package ru.practicum.mainservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.category.dto.CategoryDto;
import ru.practicum.mainservice.model.compilation.dto.CompilationDto;
import ru.practicum.mainservice.model.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.model.user.dto.UserDto;
import ru.practicum.mainservice.searchparams.PresentationParameters;
import ru.practicum.mainservice.searchparams.SearchParametersAdmin;
import ru.practicum.mainservice.service.AdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    /**
     * Категории
     */

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Контроллер админа получил запрос на добавление новой категории");
        return adminService.saveCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Integer catId) {
        log.info("Контроллер админа получил запрос на удаление категории с id = {}", catId);
        adminService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Контроллер админа получил запрос на обновление Category id = {}", catId);
        return adminService.updateCategory(catId, categoryDto);
    }

    /**
     * События
     */

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
        return adminService.getEventsWithFilteringForAdmin(searchParametersAdmin, presentationParameters);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Контроллер админа получил запрос на обновление Event id = {}", eventId);
        return adminService.updateEvent(eventId, updateEventRequest);
    }

    /**
     * Пользователи
     */

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Контроллер админа получил запрос на вывод списка пользователей");
        return adminService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        log.info("Контроллер админа получил запрос на добавление нового пользователя");
        return adminService.saveUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Integer userId) {
        log.info("Контроллер админа получил запрос на удаление пользователя с id = {}", userId);
        adminService.deleteUser(userId);
    }

    /**
     * Подборки событий
     */

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Контроллер админа получил запрос на добавление новой подборки событий");
        return adminService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Integer compId) {
        log.info("Контроллер админа получил запрос на удаление подборки с id = {}", compId);
        adminService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Integer compId,
                                            @RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Контроллер админа получил запрос на обновление подборки событий");
        return adminService.updateCompilation(compId, newCompilationDto);
    }

}
