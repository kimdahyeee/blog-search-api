package com.search.blog.api.domain.blog.dto;

import com.search.blog.api.core.error.exception.InvalidRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlogSearchRequest {
    private static final int PAGE_MIN_VALUE = 1;
    private static final int PAGE_MAX_VALUE = 50;

    @NotEmpty
    private String keyword;
    private Sort sort;
    private Integer page;

    public BlogSearchRequest(String keyword, Sort sort, Integer page) {
        validateMaxPage(page);
        this.keyword = keyword;
        this.sort = sort;
        this.page = page;
    }

    private void validateMaxPage(Integer page) {
        if (page < PAGE_MIN_VALUE) {
            throw new InvalidRequestException("page is less than " + PAGE_MIN_VALUE);
        }

        if (page > PAGE_MIN_VALUE) {
            throw new InvalidRequestException("page is more than " + PAGE_MAX_VALUE);
        }

    }

    public static BlogSearchRequest of(String keyword, Sort sort, Integer page) {
        return new BlogSearchRequest(keyword, sort, page);
    }

    public boolean isFirstPage() {
        return this.page == PAGE_MIN_VALUE;
    }

    public enum Sort {
        ACCURACY("정확도순"), RECENCY("최신순");

        private String desc;

        Sort(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return this.name().toLowerCase();
        }
    }

}
