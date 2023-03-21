package com.search.blog.model.entity.blog.exception;

import com.search.blog.core.exception.BusinessException;

public class BlogKeywordNotCreateException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "블로그 키워드 통계 테이블에 데이터를 저장할 수 없습니다.";

    public BlogKeywordNotCreateException() {
        super(DEFAULT_MESSAGE);
    }

    public BlogKeywordNotCreateException(String message) {
        super(message);
    }

}
