package com.search.blog.api.domain.blog.repository;

import com.search.blog.api.domain.blog.cache.domain.TopKeyword;
import com.search.blog.api.global.cache.blog.CacheKey;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * cacheManager 별도로 구현한 이유 ?
 * - 처음에는 간단하게 @Cacheable 캐싱 처리하려고 했는데, top 10 키워드를 직접 (직관적으로) 제어하기 위해 변경 ..!
 */
@Repository
@RequiredArgsConstructor
public class TopKeywordRepository {
    private final RedisTemplate<String, TopKeyword> redisTemplate;

    public boolean isContains(String keyword) {
        List<TopKeyword> members = this.findAll();

        return members.stream()
                .anyMatch(topKeyword -> topKeyword.getKeyword().equals(keyword));
    }

    public Long rightPush(TopKeyword topKeyword) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPush(CacheKey.TOP_KEYWORDS, topKeyword))
                .orElseThrow(() -> new CacheException("rPush operation doesn't work"));
    }

    public List<TopKeyword> findAll() {
        // refactor. size 가 null 일 수 있으니, null-safe 하게 변경
        Long size = redisTemplate.opsForList().size(CacheKey.TOP_KEYWORDS);
        List<TopKeyword> topKeywords = new ArrayList<>();

        if (size != null && size > 0) {
            topKeywords = redisTemplate.opsForList().range(CacheKey.TOP_KEYWORDS, 0, size);
        }

        return topKeywords;
    }

    public void delete() {
        redisTemplate.delete(CacheKey.TOP_KEYWORDS);
    }
}
