package ru.practicum.mainservice.mappers;

import ru.practicum.mainservice.model.compilation.Compilation;
import ru.practicum.mainservice.model.compilation.dto.CompilationDto;
import ru.practicum.mainservice.model.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.model.event.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> eventsForCompilation) {
        return new Compilation(
                newCompilationDto.getId() != null ? newCompilationDto.getId() : 0,
                newCompilationDto.getTitle() != null ? newCompilationDto.getTitle() : "",
                newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false,
                eventsForCompilation
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
