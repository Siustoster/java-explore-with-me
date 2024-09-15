package ru.practicum.mainservice.model.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.model.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    protected Integer id;
    protected List<EventShortDto> events;
    protected Boolean pinned;
    protected String title;
}
