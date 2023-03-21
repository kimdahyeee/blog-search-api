package com.search.blog.api.external.blog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

/**
 * 카카오 블로그 검색하기 response 정의
 *
 * @reference https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-response
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PUBLIC)
public class KakaoBlogSearchApiError {
    @JsonProperty("message")
    private String message;

    @JsonProperty("code")
    private Integer code;
}
