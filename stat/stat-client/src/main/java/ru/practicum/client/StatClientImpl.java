package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatClientImpl implements StatClient {
    @Value("${stats.server.url}")
    private String statUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void saveStat(ServerHttpRequest request, String appName) {

        HitRequestDto hit = new HitRequestDto(appName, request.getURI().getRawPath(),
                Objects.requireNonNull(request.getRemoteAddress()).toString(), LocalDateTime.now().format(formatter));

        String resp = WebClient.create().post().uri(statUrl + "/hit").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(hit), HitRequestDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<StatDto> getStat(String start,
                                 String end,
                                 List<String> uri,
                                 Boolean unique) {
        log.info("get stat");
        List<StatDto> outDto = Arrays.stream(WebClient.create(statUrl).get().
                uri(uriBuilder -> uriBuilder.
                        path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uri)
                        .queryParam("unique", unique)
                        .build()
                )
                .retrieve()
                .bodyToMono(StatDto[].class)
                .block()).toList();
        return outDto;
    }
}

