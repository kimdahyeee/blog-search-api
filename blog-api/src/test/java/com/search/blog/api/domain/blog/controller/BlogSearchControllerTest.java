package com.search.blog.api.domain.blog.controller;

import com.search.blog.api.core.error.response.ErrorCode;
import com.search.blog.api.test.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
class BlogSearchControllerTest extends IntegrationTest {

    private static final String API_BLOG_SEARCH = "/api/v1/search/blog";
    private static final String API_BLOG_TOP_KEYWORDS = "/api/v1/search/blog/keywords";
    private static final int FETCH_BLOG_SIZE = 10;

    @Test
    @DisplayName("블로그 검색 시 http status 200 과 블로그 정보를 응답한다.")
    void 성공_블로그검색() throws Exception {
        mockMvc.perform(get(API_BLOG_SEARCH)
                .param("keyword", "댕댕이")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.blogResponses.datas", is(hasSize(FETCH_BLOG_SIZE))));
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 52, 53, -1, 0, -2})
    @DisplayName("블로그 검색 시 page 는 1 이상 50 이하이어야한다. 아닌 경우 400 상태와 사유를 반환한다.")
    void 실패_블로그검색_페이지(int page) throws Exception {
        mockMvc.perform(get(API_BLOG_SEARCH)
                .param("keyword", "댕댕이")
                .param("page", String.valueOf(page))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @DisplayName("인기 검색어 목록 조회 시 http status 200 과 검색어 정보를 응답한다.")
    void 성공_top10() throws Exception {
        mockMvc.perform(get(API_BLOG_TOP_KEYWORDS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.topKeywords", hasSize(0)));
    }
}