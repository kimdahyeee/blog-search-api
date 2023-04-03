package com.search.blog.api.domain.blog.service;

import com.search.blog.api.domain.blog.cache.domain.TopKeyword;
import com.search.blog.api.domain.blog.dto.KeywordResponse;
import com.search.blog.api.domain.blog.repository.BlogKeywordRepository;
import com.search.blog.api.domain.blog.repository.TopKeywordRepository;
import com.search.blog.api.global.cache.blog.CacheKey;
import com.search.blog.model.entity.blog.BlogKeyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.search.blog.core.logger.KbLogger.message;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeywordService {

    private static final int COUNT_OF_ONE_REQUEST = 1;
    private static final int COUNT_OF_TOP_KEYWORDS = 10;
    private static final int FIRST_INDEX_OF_TOP_KEYWORDS = 0;

    private final RedisTemplate<String, String> redisTemplate;
    private final BlogKeywordRepository blogKeywordRepository;
    private final TopKeywordRepository topKeywordRepository;

    public boolean containCacheWith(String keyword) {
        return topKeywordRepository.isContains(keyword);
    }

    @Transactional
    public void saveBySearch(String keyword) {
        saveKeyword(keyword);
        this.countingByRequest(keyword);
    }

    /**
     * `redisTemplate.opsForZSet()` SortedSet 자료구조 사용 - score 을 가지고 있기 때문에, 순위라는 개념을 적용하기에 적절
     * - 그에 맞는 range 메소드도 제공하고 있음
     */
    private void countingByRequest(String keyword) {
        redisTemplate.opsForZSet().incrementScore(CacheKey.KEYWORDS_SET, keyword, COUNT_OF_ONE_REQUEST);
    }

    private void saveKeyword(String keyword) {
        Optional<BlogKeyword> saved = blogKeywordRepository.findByKeyword(keyword);

        if (saved.isEmpty()) {
            blogKeywordRepository.save(BlogKeyword.create(keyword));
            return;
        }

        BlogKeyword savedBlogKeyword = saved.get();
        savedBlogKeyword.updateSearchCount();
    }

    public KeywordResponse fetchTopKeywords() {
        Set<ZSetOperations.TypedTuple<String>> results = redisTemplate.opsForZSet().reverseRangeWithScores(CacheKey.KEYWORDS_SET, FIRST_INDEX_OF_TOP_KEYWORDS, COUNT_OF_TOP_KEYWORDS - 1);

        List<KeywordResponse.Response> responses = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : results) {
            String keyword = tuple.getValue();
            double score = tuple.getScore();

            responses.add(KeywordResponse.Response.of(keyword, Double.valueOf(score).intValue()));
        }

        return KeywordResponse.of(responses);
    }

    public void cachingTopKeywords(KeywordResponse topKeywords) {
        try {
            topKeywordRepository.delete();
            topKeywords.getTopKeywords()
                    .forEach(topKeyword -> topKeywordRepository.rightPush(TopKeyword.of(topKeyword.getKeyword(), topKeyword.getCount())));
        } catch (Exception e) {
            log.error(message("fail save top keywords to redis "), e);
        }
    }

}
