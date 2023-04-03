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

    /**
     * 외부 API 장애 상황 고려 (1)
     * - 장애 상황을 고려하여 4xx 응답인 경우 3번의 재시도 요청을 한다.
     *
     * ***
     * - aop 기반으로 동작한다.
     * - recover 은 retry 가 모두 실패한 후 실행될 메소드
     */
    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000), recover = "recover1")
    public BlogSearchResponse fetchBlogs(KakaoBlogSearchApiRequest kakaoBlogSearchApiRequest) {
        KakaoBlogSearchApiResponse items = this.callFetchBlogs(kakaoBlogSearchApiRequest);

        return BlogSearchResponse.of(kakaoBlogSearchApiRequest.getPage(), items);
    }

    private KakaoBlogSearchApiResponse callFetchBlogs(KakaoBlogSearchApiRequest blogRequest) throws HttpRequestException {
        return get(KakaoBlogSearchApiResponse.class, blogSearchKakaoApiConfig.getUri(), blogRequest.getQueryParams());
    }

    /**
     * 카카오 api 가 4xx 응답을 반환할 때, 상세 에러를 확인하기 위해 KakaoBlogSearchApiError 를 정의했고,
     * logging 처리와 재시도가 가능하도록 RetryException 을 반환하도록 구현
     *
     * * 4xx 일 때만 재시도 한 이유
     * -> 카카오에서 내려주는 상세 코드에 따라 재시도해야하는 상황만 재시도 하려는 의도였는데, api 문서에 정의되어있는 response 객체가 달라서, 4xx 일괄 재시도하도록 적용
     * -> 굳이 장애 상황이고 재시도를 하지 않아야하는 경우엔 재시도 해야할 필요성이 없다고 판단함
     */
    @Override
    protected Function<ClientResponse, Mono<? extends Throwable>> get4xxErrorHandlingMono(String uri) {
        return clientResponse -> clientResponse.bodyToMono(KakaoBlogSearchApiError.class)
                .flatMap(error -> {
                    log.error(message("fail to request kakao api", KvData.of("detailMessage", error.getMessage())));

                    return Mono.error(new RetryException(error.getMessage()));
                });
    }
}
