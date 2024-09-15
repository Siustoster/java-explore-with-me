package ru.practicum.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.mappers.CategoryMapper;
import ru.practicum.mainservice.mappers.CompilationMapper;
import ru.practicum.mainservice.model.category.dto.CategoryDto;
import ru.practicum.mainservice.model.compilation.dto.CompilationDto;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.searchparams.PresentationParameters;
import ru.practicum.mainservice.searchparams.SearchParametersUsersPublic;
import ru.practicum.mainservice.service.entity.CategoryService;
import ru.practicum.mainservice.service.entity.CompilationService;
import ru.practicum.mainservice.service.entity.EventService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PublicService {
    private final EventService eventService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    /**
     * Подборки событий
     */

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(int compId) {
        return CompilationMapper.toCompilationDto(compilationService.getCompilation(compId));
    }

    /**
     * Категории
     */

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryService.getCategories(from, size);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(int catId) {
        return CategoryMapper.toCategoryDto(categoryService.getCategory(catId));
    }

    /**
     * События
     */

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFiltering(SearchParametersUsersPublic searchParametersUsersPublic,
                                                      PresentationParameters presentationParameters,
                                                      HttpServletRequest servletRequest) {
        return eventService.getEventsWithFilteringForPublic(searchParametersUsersPublic, presentationParameters, servletRequest);
    }

    @Transactional(readOnly = true)
    public EventFullDto getEventForPublic(int id, HttpServletRequest servletRequest) {
        return eventService.getEventForPublic(id, servletRequest);
    }
}
