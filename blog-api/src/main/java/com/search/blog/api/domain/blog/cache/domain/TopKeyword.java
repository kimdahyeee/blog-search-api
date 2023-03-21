package com.search.blog.api.domain.blog.cache.domain;

import com.search.blog.api.global.cache.blog.CacheKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@RedisHash(value = CacheKey.TOP_KEYWORDS, timeToLive = 300) // 5 minute
@NoArgsConstructor
@AllArgsConstructor
public class TopKeyword implements Serializable {
    @Id
    private String keyword;
    private int count;

    public static TopKeyword of(String keyword, int count) {
        return new TopKeyword(keyword, count);
    }

}
