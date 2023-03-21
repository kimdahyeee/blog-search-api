package com.search.blog.api.domain.blog.repository;

import com.search.blog.api.domain.blog.cache.domain.TopKeyword;
import com.search.blog.api.global.cache.blog.CacheKey;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return redisTemplate.opsForList().range(CacheKey.TOP_KEYWORDS, 0, redisTemplate.opsForList().size(CacheKey.TOP_KEYWORDS));
    }

    public void delete() {
        redisTemplate.delete(CacheKey.TOP_KEYWORDS);
    }
}
