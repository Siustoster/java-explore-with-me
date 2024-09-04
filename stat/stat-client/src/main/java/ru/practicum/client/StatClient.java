package ru.practicum.client;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface StatClient {
    String saveStat(ServerHttpRequest request, String appName);

}
