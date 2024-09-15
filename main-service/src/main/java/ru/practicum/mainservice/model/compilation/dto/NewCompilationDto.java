package ru.practicum.mainservice.model.compilation.dto;

import lombok.*;

import jakarta.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewCompilationDto {
    protected Integer id;
    protected List<Integer> events;
    protected Boolean pinned;
    @Size(min = 1, max = 50)
    protected String title;
}
