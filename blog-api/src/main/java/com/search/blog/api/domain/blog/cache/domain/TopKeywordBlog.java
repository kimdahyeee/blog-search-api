package com.search.blog.api.domain.blog.cache.domain;

import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash(value = "topKeywordBlog", timeToLive = 30)
@NoArgsConstructor
@AllArgsConstructor
public class TopKeywordBlog implements Serializable {
    @Id
    private String keyword;
    BlogSearchResponse BlogSearchResponse;

    public static TopKeywordBlog of(String keyword, BlogSearchResponse BlogSearchResponse) {
        return new TopKeywordBlog(keyword, BlogSearchResponse);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Blog {
        private String title;
        private String contents;
        private String url;
        private String blogName;
        private String thumbnail;
        private LocalDateTime dateTime;

        public static Blog of(String title, String contents, String url, String blogName, String thumbnail, LocalDateTime dateTime) {
            return new Blog(title, contents, url, blogName, thumbnail, dateTime);
        }
    }
}
