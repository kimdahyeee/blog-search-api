package com.search.blog.api.external.blog;

import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.external.ApiCallClient;
import com.search.blog.api.external.blog.config.BlogSearchApiConfig;
import com.search.blog.api.external.blog.request.KakaoBlogSearchApiRequest;
import com.search.blog.api.external.blog.response.KakaoBlogSearchApiError;
import com.search.blog.api.external.blog.response.KakaoBlogSearchApiResponse;
import com.search.blog.core.exception.HttpRequestException;
import com.search.blog.core.logger.data.KvData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.search.blog.core.logger.KbLogger.message;

@Slf4j
@Service
public class KakaoBlogSearchClient extends ApiCallClient implements BlogSearchClient<KakaoBlogSearchApiRequest> {

    private final BlogSearchApiConfig blogSearchKakaoApiConfig;

    public KakaoBlogSearchClient(BlogSearchApiConfig blogSearchKakaoApiConfig) {
        super(blogSearchKakaoApiConfig.getUrl(), blogSearchKakaoApiConfig.getDefaultHeaders());
        this.blogSearchKakaoApiConfig = blogSearchKakaoApiConfig;
    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000), recover = "recover1")
    public BlogSearchResponse fetchBlogs(KakaoBlogSearchApiRequest kakaoBlogSearchApiRequest) {
        KakaoBlogSearchApiResponse items = this.callFetchBlogs(kakaoBlogSearchApiRequest);

        return BlogSearchResponse.of(kakaoBlogSearchApiRequest.getPage(), items);
    }

    private KakaoBlogSearchApiResponse callFetchBlogs(KakaoBlogSearchApiRequest blogRequest) throws HttpRequestException {
        return get(KakaoBlogSearchApiResponse.class, blogSearchKakaoApiConfig.getUri(), blogRequest.getQueryParams());
    }

    @Override
    protected Function<ClientResponse, Mono<? extends Throwable>> get4xxErrorHandlingMono(String uri) {
        return clientResponse -> clientResponse.bodyToMono(KakaoBlogSearchApiError.class)
                .flatMap(error -> {
                    log.error(message("fail to request kakao api", KvData.of("detailMessage", error.getMessage())));

                    return Mono.error(new RetryException(error.getMessage()));
                });
    }
}
