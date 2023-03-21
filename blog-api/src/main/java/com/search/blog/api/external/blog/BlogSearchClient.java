package com.search.blog.api.external.blog;

import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.external.blog.request.BlogSearchApiRequest;

public interface BlogSearchClient<T extends BlogSearchApiRequest> {
    /**
     * 블로그 검색 API 호출하는 client 메소드
     */
    public BlogSearchResponse fetchBlogs(T kakaoBlogSearchApiRequest);
}
