package ru.practicum.client;

import org.springframework.http.server.reactive.ServerHttpRequest;
import ru.practicum.explorewme.StatDto;

import java.util.List;

public interface StatClient {
    void saveStat(ServerHttpRequest request, String appName);

    public List<StatDto> getStat(String start,
                           String end,
                           List<String> uri,
                           Boolean unique);

}
