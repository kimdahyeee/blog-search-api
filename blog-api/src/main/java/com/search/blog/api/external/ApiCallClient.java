package com.search.blog.api.external;

import com.search.blog.core.exception.HttpRequestException;
import com.search.blog.core.logger.data.KvData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.RetryException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * api 공통 클라이언트 정의
 * - 기본 타임아웃 처리는 5s 로 설정한다.
 *
 * @throws 4xx, 5xx 에러인 경우 {@link HttpRequestException} throw
 */
@Slf4j
public class ApiCallClient {

    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_TIME = 1;

    protected final WebClient webClient;

    public ApiCallClient(String baseUrl, Consumer<HttpHeaders> defaultHeaders) {
        this.webClient = WebClient.builder()
                .clientConnector(getConnector(HttpTimeout.DEFAULT_HTTP_TIMEOUT))
                .baseUrl(baseUrl)
                .defaultHeaders(defaultHeaders)
                .build();
    }

    private ReactorClientHttpConnector getConnector(HttpTimeout httpTimeout) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpTimeout.getConnectTimeoutMillis())
                                .doOnConnected(connection ->
                                        connection.addHandlerLast(new ReadTimeoutHandler(httpTimeout.getReadTimeoutSecond()))
                                                .addHandlerLast(new WriteTimeoutHandler(httpTimeout.getWriteTimeoutSecond()))));

        return new ReactorClientHttpConnector(httpClient);
    }

    protected <T> T get(final ParameterizedTypeReference<T> elementTypeRef, final String uri) throws HttpRequestException {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::isError, get5xxErrorHandlingMono(uri))
                .bodyToMono(elementTypeRef)
                .block();
    }

    protected <T> T get(final Class<T> clazz, final String uri, Object... uriVariables) throws HttpRequestException {
        return webClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatus::isError, get5xxErrorHandlingMono(uri))
                .bodyToMono(clazz)
                .block();
    }

    protected <T> T get(final Class<T> clazz, final String uri, MultiValueMap<String, String> queryParams) throws HttpRequestException, RetryException {
        String requestUri = UriComponentsBuilder.fromUriString(uri)
                .queryParams(queryParams)
                .build()
                .toUriString();

        return webClient.get()
                .uri(requestUri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, get4xxErrorHandlingMono(requestUri))
                .onStatus(HttpStatus::is5xxServerError, get5xxErrorHandlingMono(requestUri))
                .bodyToMono(clazz)
                .block();
    }

    protected <T> T callApi(final Class<T> clazz, final String uri, MultiValueMap<String, String> queryParams) throws HttpRequestException, RetryException {
        String requestUri = UriComponentsBuilder.fromUriString(uri)
                .queryParams(queryParams)
                .build()
                .toUriString();

        int retryCount = 0;
        return Stream.iterate(0, i -> i + 1)
                .limit(retryCount + 1)
                .map(count -> callByOnce(clazz, requestUri, retryCount, count))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new HttpRequestException(KvData.of("uri", uri)));
    }


    private <T> Optional<T> callByOnce(final Class<T> clazz, final String uri, final int retryCount, int attempt) throws HttpRequestException, RetryException {
        try {
            return Optional.ofNullable(webClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, get4xxErrorHandlingMono(uri))
                    .onStatus(HttpStatus::is5xxServerError, get5xxErrorHandlingMono(uri))
                    .bodyToMono(clazz)
                    .block());
        } catch (RetryException e) {
            if (attempt < retryCount) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }


    protected Function<ClientResponse, Mono<? extends Throwable>> get4xxErrorHandlingMono(final String uri) {
        return __ -> Mono.error(new RetryException("fail to retry !"));
    }

    protected Function<ClientResponse, Mono<? extends Throwable>> get5xxErrorHandlingMono(final String uri) {
        return __ -> Mono.error(new HttpRequestException(KvData.of("uri", uri)));
    }

}
