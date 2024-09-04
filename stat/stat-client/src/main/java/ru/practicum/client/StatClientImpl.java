package ru.practicum.client;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import ru.practicum.explorewme.HitRequestDto;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatClientImpl implements StatClient {
    @Value("${stats.server.url}")
    private String statUrl;
    WebClient client = WebClient.create(statUrl);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public String saveStat(ServerHttpRequest request, String appName) {
       // WebClient client = WebClient.create(statUrl);
        log.info(statUrl);
        WebClient.UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        //URI uri = URI.create(statUrl+"/hit")
        RequestBodySpec bodySpec = uriSpec.uri(URI.create(statUrl+"/hit"));

        HitRequestDto hit = new HitRequestDto(appName, request.getURI().getRawPath(),
                Objects.requireNonNull(request.getRemoteAddress()).toString(), LocalDateTime.now().format(formatter));

        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromValue(hit));

        WebClient.ResponseSpec responseSpec = headersSpec.header(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();

        return responseSpec.toString();
    }
}

