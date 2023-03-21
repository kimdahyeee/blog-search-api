package com.search.blog.model.entity.blog;

import com.search.blog.core.exception.BusinessException;
import com.search.blog.model.entity.BaseEntity;
import com.search.blog.model.entity.blog.exception.BlogKeywordNotCreateException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

/**
 * 블로그 키워드 통계 테이블
 */
@Entity
@Getter
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PRIVATE)
@Table(name = "blog_keyword")
public class BlogKeyword extends BaseEntity {

    private static final Long DEFAULT_COUNT = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "keyword_no")
    private Long no;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "keyword_count")
    private Long count;

    private BlogKeyword(String keyword, Long count) {
        validKeywordCount(count);
        this.keyword = keyword;
        this.count = count;
    }

    private void validKeywordCount(Long count) {
        if (count < 1L) {
            throw new BlogKeywordNotCreateException("keyword count is not valid : " + count);
        }
    }

    public static BlogKeyword create(String keyword) {
        return new BlogKeyword(keyword, DEFAULT_COUNT);
    }

    public void updateSearchCount() {
        if (count == null) {
            throw new BusinessException();
        }

        this.count++;
    }

}

