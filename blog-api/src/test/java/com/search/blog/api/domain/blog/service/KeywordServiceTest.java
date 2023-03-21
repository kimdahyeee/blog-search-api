package com.search.blog.api.domain.blog.service;

import com.search.blog.api.domain.blog.dto.KeywordResponse;
import com.search.blog.api.global.cache.blog.CacheKey;
import com.search.blog.api.test.helper.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@DisplayName("레디스 동작 테스트")
class KeywordServiceTest extends IntegrationTest {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setup() {
        redisTemplate.delete(CacheKey.KEYWORDS_SET);
    }

    @Test
    @DisplayName("특정 키워드에 대해 검색 할 때마다 score 를 1 씩 증가시켜준다.")
    void saveScore() {
        //given
        String keyword = "댕댕이";

        //when
        int count = 5;
        IntStream.range(0, count)
                .forEach(i -> keywordService.saveBySearch(keyword));

        //then
        Double score = redisTemplate.opsForZSet().score(CacheKey.KEYWORDS_SET, keyword);
        assertEquals(count, Double.valueOf(score).intValue());
    }

    @Test
    @DisplayName("인기 검색어 목록을 확인할 수 있다.")
    void fetchTopKeywords() {
        //given
        String keyword = "댕댕이";
        String keyword2 = "고영희";

        //when
        int keyword_count = 5;
        IntStream.range(0, keyword_count)
                .forEach(i -> keywordService.saveBySearch(keyword));


        int keyword2_count = 2;
        IntStream.range(0, keyword2_count)
                .forEach(i -> keywordService.saveBySearch(keyword2));

        //then
        KeywordResponse topKeywords = keywordService.fetchTopKeywords();
        assertEquals(2, topKeywords.getTopKeywords().size());
        assertEquals(keyword, topKeywords.getTopKeywords().get(0).getKeyword());
        assertEquals(keyword_count, topKeywords.getTopKeywords().get(0).getCount());
        System.out.println("topKeywords.toString() = " + topKeywords.toString());
    }


    @Test
    @DisplayName("인기 검색어 목록은 최대 10개까지 확인 가능하다.")
    void fetchTopKeywords_maxCount() {
        //given
        String keyword = "댕댕이";

        //when
        int max_keyword_count = 10;
        IntStream.range(0, max_keyword_count + 5)
                .forEach(i -> keywordService.saveBySearch(keyword + i));

        //then
        KeywordResponse topKeywords = keywordService.fetchTopKeywords();
        assertEquals(max_keyword_count, topKeywords.getTopKeywords().size());
        System.out.println("topKeywords.toString() = " + topKeywords.toString());
    }
}