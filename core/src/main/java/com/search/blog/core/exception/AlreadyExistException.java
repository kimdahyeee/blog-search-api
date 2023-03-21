package com.search.blog.core.exception;

/**
 * {@code AlreadyExistException} 은 데이터베이스에 중복된 데이터가 존재하는 경우 반환한다.
 * <p>
 * 필요에따라 {@code AlreadyExistException}을 상속하여 custom exception 을 구현할 수 있다.
 */
public class AlreadyExistException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "이미 존재하는 데이터 입니다.";

    public AlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyExistException(String message) {
        super(message);
    }

}
