package com.search.blog.api.domain.blog.service;

import com.search.blog.api.domain.blog.cache.domain.TopKeywordBlog;
import com.search.blog.api.domain.blog.dto.BlogSearchRequest;
import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.domain.blog.repository.TopKeywordBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.search.blog.core.logger.KbLogger.message;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchService {
    private final BlogSearchClientService blogSearchClientService;
    private final KeywordService keywordService;
    private final TopKeywordBlogRepository topKeywordBlogRepository;

    /**
     * 블로그 검색 결과 반환 메소드
     * - 캐시 값이 있는 경우 api 요청을 하지 않는다.
     */
    public BlogSearchResponse fetchBlogs(BlogSearchRequest blogSearchRequest) {
        return cachingTopKeywordResults(blogSearchRequest)
                .map(TopKeywordBlog::getBlogSearchResponse)
                .orElseGet(() -> {
                    BlogSearchResponse blogSearchResponse = blogSearchClientService.fetchBlogs(blogSearchRequest);
                    keywordService.saveBySearch(blogSearchRequest.getKeyword());
                    topKeywordBlogRepository.save(TopKeywordBlog.of(blogSearchRequest.getKeyword(), blogSearchResponse));

                    return blogSearchResponse;
                });
    }

    /**
     * 인기 검색어 캐싱 기준
     * - 인기 검색어인 경우
     * - 첫 페이지인 경우
     */
    private Optional<TopKeywordBlog> cachingTopKeywordResults(BlogSearchRequest blogSearchRequest) {
        try {
            if (blogSearchRequest.isFirstPage() && keywordService.containCacheWith(blogSearchRequest.getKeyword())) {
                return topKeywordBlogRepository.findById(blogSearchRequest.getKeyword());
            }
        } catch (RuntimeException e) {
            log.error(message("cache fetch error"), e);
        }

        return Optional.empty();
    }
}
