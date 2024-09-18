package ru.practicum.mainservice.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchParametersAdmin {
    protected List<Integer> users;
    protected List<EventState> states;
    protected List<Integer> categories;
    protected LocalDateTime rangeStart;
    protected LocalDateTime rangeEnd;
}
