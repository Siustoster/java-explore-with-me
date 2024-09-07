package ru.practicum.explorewme;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    String app;
    String uri;
    Long hits;
}
