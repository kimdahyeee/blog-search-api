package com.search.blog.api.core.error.exception;

import com.search.blog.core.exception.BusinessException;
import org.springframework.validation.FieldError;

/**
 * {@code InvalidRequestException} 은 요청 값이 비지니스 로직을 더이상 수행할 수 없는 경우 반환된다.
 * 주로, validation 에 어긋나는 경우 사용된다.
 * <p>
 * 필요에따라 {@code InvalidRequestException}을 상속하여 custom exception 을 구현할 수 있다.
 */
public class InvalidRequestException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "잘못된 요청입니다.";
    private FieldError fieldError;

    public InvalidRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(FieldError fieldError) {
        super(DEFAULT_MESSAGE);
        this.fieldError = fieldError;
    }

    public FieldError getFieldError() {
        return fieldError;
    }

}
