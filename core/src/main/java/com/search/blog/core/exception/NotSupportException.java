package com.search.blog.core.exception;

/**
 * {@code NotSupportException} 은 서비스에서 지원하지 않는 요청이 발생한 경우 반환된다. (예를 들어 enum not found)
 * <p>
 * 필요에따라 {@code NotSupportException}을 상속하여 custom exception 을 구현할 수 있다.
 */
public class NotSupportException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "지원되지 않는 요청입니다.";

    public NotSupportException() {
        super(DEFAULT_MESSAGE);
    }

    public NotSupportException(String message) {
        super(message);
    }

}
