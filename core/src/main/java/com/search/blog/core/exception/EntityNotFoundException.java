package com.search.blog.core.exception;

/**
 * {@code DataNotFoundException} 은 타겟 데이터가 존재하지 않는 경우 반환한다.
 * <p>
 * 필요에따라 {@code DataNotFoundException}을 상속하여 custom exception 을 구현할 수 있다.
 */
public class EntityNotFoundException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "데이터가 존재하지 않습니다.";

    public EntityNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
