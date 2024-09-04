package ru.practicum.client;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

public interface StatsClient {
    String saveStat(ServerHttpRequest request, String appName);

}
