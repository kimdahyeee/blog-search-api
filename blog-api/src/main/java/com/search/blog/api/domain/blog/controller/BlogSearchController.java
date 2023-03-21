package com.search.blog.api.domain.blog.controller;

import com.search.blog.api.core.response.OkResponse;
import com.search.blog.api.domain.blog.dto.BlogSearchRequest;
import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.domain.blog.dto.KeywordResponse;
import com.search.blog.api.domain.blog.service.BlogSearchService;
import com.search.blog.api.domain.blog.service.KeywordService;
import com.search.blog.api.global.cache.blog.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search/blog")
public class BlogSearchController {

    private final BlogSearchService blogSearchService;
    private final KeywordService keywordService;

    /**
     * 블로그 검색 API
     */
    @GetMapping
    public OkResponse<BlogSearchResponse> fetchBlogInfos(@RequestParam String keyword,
                                                         @RequestParam(required = false, defaultValue = "ACCURACY") BlogSearchRequest.Sort sort,
                                                         @RequestParam(required = false, defaultValue = "1") Integer page) {
        BlogSearchResponse blogSearchResponse = blogSearchService.fetchBlogs(BlogSearchRequest.of(keyword, sort, page));

        return OkResponse.of(blogSearchResponse);
    }

    /**
     * 인기 검색어 목록 API
     * - 검색어는 5분 간 캐싱한다. @todo
     */
    @Cacheable(value = CacheKey.TOP_KEYWORDS)
    @GetMapping("/keywords")
    public OkResponse<KeywordResponse> fetchTopKeywords() {
        KeywordResponse topKeywords = keywordService.fetchTopKeywords();
        keywordService.cachingTopKeywords(topKeywords);
        return OkResponse.of(topKeywords);
    }

}
