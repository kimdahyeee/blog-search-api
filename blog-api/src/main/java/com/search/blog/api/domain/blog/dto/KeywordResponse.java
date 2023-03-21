package com.search.blog.api.domain.blog.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordResponse implements Serializable {

    private static final long serialVersionUID = 835613172520787168L;

    private List<Response> topKeywords;

    public static KeywordResponse of(List<Response> responses) {
        return new KeywordResponse(responses);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response implements Serializable {
        private String keyword;
        private int count;

        public static Response of(String keyword, int count) {
            return new Response(keyword, count);
        }
    }
}
