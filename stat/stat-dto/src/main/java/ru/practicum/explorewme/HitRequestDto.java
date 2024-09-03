package ru.practicum.explorewme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HitRequestDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
