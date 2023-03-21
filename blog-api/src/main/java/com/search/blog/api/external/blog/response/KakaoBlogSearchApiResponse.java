package com.search.blog.api.external.blog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

/**
 * 카카오 블로그 검색하기 response 정의
 *
 * @reference https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-response
 */
@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PUBLIC)
public class KakaoBlogSearchApiResponse {
    private List<KakaoBlogSearchApiItem> documents;
    private Meta meta;

    public Integer getTotalCount() {
        return this.meta.getTotalCount();
    }

    public List<KakaoBlogSearchApiItem> getDocuments() {
        return documents;
    }

    @Getter
    public class Meta {
        @JsonProperty("is_end")
        private boolean isEnd;
        @JsonProperty("pageable_count")
        private Integer pageableCount;
        @JsonProperty("total_count")
        private Integer totalCount;
    }
}
