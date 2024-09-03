package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import reactor.core.publisher.Mono;
import ru.practicum.explorewme.HitRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatClient {
    private final String statUrl = "http://localhost:9090";
    private WebClient client = WebClient.create(statUrl);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveStat(HttpServletRequest request, String appName) {

        WebClient.UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        RequestBodySpec bodySpec = uriSpec.uri("/hit");
        HitRequestDto hit = new HitRequestDto(appName, request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter));
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromValue(hit));
        Mono<String> response = headersSpec.retrieve()
                .bodyToMono(String.class);

    }
}

