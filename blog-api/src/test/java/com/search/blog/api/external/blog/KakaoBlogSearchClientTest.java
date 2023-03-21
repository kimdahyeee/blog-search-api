package com.search.blog.api.external.blog;

import com.search.blog.api.domain.blog.dto.BlogSearchResponse;
import com.search.blog.api.external.blog.request.KakaoBlogSearchApiRequest;
import com.search.blog.api.test.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("kakao api response 를 확인하기 위한 테스트")
class KakaoBlogSearchClientTest extends IntegrationTest {

    @Autowired
    private KakaoBlogSearchClient kakaoBlogSearchClient;

    @Test
    @DisplayName("특정 키워드로 블로그를 검색하는 API")
    void fetchKakaoBlogs() {
        BlogSearchResponse kakaoBlogSearchApiItems = kakaoBlogSearchClient.fetchBlogs(KakaoBlogSearchApiRequest.searchBy("댕댕이"));
        System.out.println("kakaoBlogSearchApiItems = " + kakaoBlogSearchApiItems);

        assertNotNull(kakaoBlogSearchApiItems);
    }

}