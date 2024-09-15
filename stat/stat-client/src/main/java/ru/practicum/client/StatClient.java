package ru.practicum.client;

import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;

import java.util.List;

public interface StatClient {
    void saveStat(HitRequestDto hit);

    public List<StatDto> getStat(String start,
                                 String end,
                                 List<String> uri,
                                 Boolean unique);

}
