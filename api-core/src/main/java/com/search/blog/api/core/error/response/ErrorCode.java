package com.search.blog.api.core.error.response;

import lombok.Getter;

/**
 * 4xx 는 보통 클라이언트 오류
 * 5xx 는 서버 측 오류
 */
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "서비스 오류입니다.");

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
