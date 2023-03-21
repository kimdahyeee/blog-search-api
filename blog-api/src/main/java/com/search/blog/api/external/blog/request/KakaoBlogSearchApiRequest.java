package com.search.blog.api.external.blog.request;

import com.search.blog.api.domain.blog.dto.BlogSearchRequest;
import com.search.blog.core.exception.NotSupportException;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 카카오 블로그 검색하기 API request 정의
 *
 * @reference https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-request
 */
public class KakaoBlogSearchApiRequest implements BlogSearchApiRequest {
    private static final int DEFAULT_FETCH_SIZE = 10;

    private String query;
    private Sort sort = Sort.ACCURACY;
    private Integer page = 1;
    private Integer size = DEFAULT_FETCH_SIZE;

    private KakaoBlogSearchApiRequest(String query) {
        this.query = query;
    }

    @Builder(access = AccessLevel.PRIVATE, builderMethodName = "of", builderClassName = "of")
    public KakaoBlogSearchApiRequest(String query, Sort sort, Integer page, Integer size) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("query", query);
        multiValueMap.add("sort", sort.getValue());
        multiValueMap.add("page", page.toString());
        multiValueMap.add("size", size.toString());

        return multiValueMap;
    }

    public static KakaoBlogSearchApiRequest searchBy(String keyword) {
        return new KakaoBlogSearchApiRequest(keyword);
    }

    public static KakaoBlogSearchApiRequest searchBy(BlogSearchRequest blogSearchRequest) {
        return KakaoBlogSearchApiRequest.of()
                .query(blogSearchRequest.getKeyword())
                .sort(Sort.convertBy(blogSearchRequest.getSort()))
                .page(blogSearchRequest.getPage())
                .size(DEFAULT_FETCH_SIZE)
                .build();
    }

    public enum Sort {
        ACCURACY("정확도순"), RECENCY("최신순");

        private String desc;

        Sort(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return this.name().toLowerCase();
        }

        public static Sort convertBy(BlogSearchRequest.Sort sort) {
            switch (sort) {
                case ACCURACY:
                    return ACCURACY;
                case RECENCY:
                    return RECENCY;
                default:
                    throw new NotSupportException();
            }
        }
    }
}
