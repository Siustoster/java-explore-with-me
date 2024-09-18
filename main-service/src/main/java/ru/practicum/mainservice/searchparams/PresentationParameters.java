package ru.practicum.mainservice.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.enums.SortType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PresentationParameters {
    protected SortType sort;
    protected Integer from;
    protected Integer size;
}
