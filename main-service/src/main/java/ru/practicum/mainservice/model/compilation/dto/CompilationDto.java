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
    private Integer id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
