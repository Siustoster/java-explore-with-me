package ru.practicum.mainservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.model.category.dto.CategoryDto;
import ru.practicum.mainservice.service.CategoryService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Контроллер админа получил запрос на добавление новой категории");
        return categoryService.save(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Integer catId) {
        log.info("Контроллер админа получил запрос на удаление категории с id = {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Контроллер админа получил запрос на обновление Category id = {}", catId);
        return categoryService.update(catId, categoryDto);
    }
}
