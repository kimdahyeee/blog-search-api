package com.search.blog.api.domain.blog.service;

import com.search.blog.api.domain.blog.dto.BlogSearchRequest;
import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.external.blog.BlogSearchClient;
import com.search.blog.api.external.blog.request.KakaoBlogSearchApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogSearchClientService {
    private final BlogSearchClient kakaoBlogSearchClient;

    public BlogSearchResponse fetchBlogs(BlogSearchRequest blogSearchRequest) {
        BlogSearchResponse response = kakaoBlogSearchClient.fetchBlogs(KakaoBlogSearchApiRequest.searchBy(blogSearchRequest));

        //todo 실패 시 naver api call

        return response;
    }

}
