package com.search.blog.api.external.blog;

import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.external.blog.request.BlogSearchApiRequest;

/**
 * 외부 API 장애 상황 고려 (2)
 * - 공통 request/response 정의 ( -> 추후 백업 API 적용되었을 때 일괄적으로 처리하기 위함 )
 */
public interface BlogSearchClient<T extends BlogSearchApiRequest> {
    /**
     * 블로그 검색 API 호출하는 client 메소드
     */
    public BlogSearchResponse fetchBlogs(T request);
}
