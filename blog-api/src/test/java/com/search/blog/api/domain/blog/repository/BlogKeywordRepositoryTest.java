package com.search.blog.api.domain.blog.repository;

import com.search.blog.model.entity.blog.BlogKeyword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.helper.RepositoryTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlogKeywordRepositoryTest extends RepositoryTest {

    @Autowired
    private BlogKeywordRepository blogKeywordRepository;

    @Test
    @DisplayName("blog_keyword 테이블에 데이터를 저장하고 값을 확인 할 수 있다.")
    void saveAndFetch() {
        //given
        int count = 1;
        String keyword = "댕댕이";

        //when
        BlogKeyword blogKeyword = BlogKeyword.create(keyword);
        blogKeywordRepository.save(blogKeyword);

        Optional<BlogKeyword> saved = blogKeywordRepository.findByKeyword(keyword);

        //then
        Assertions.assertTrue(saved.isPresent());
        assertEquals(keyword, saved.map(BlogKeyword::getKeyword).orElseThrow());
        assertEquals(count, saved.map(BlogKeyword::getCount).orElseThrow());
    }

}