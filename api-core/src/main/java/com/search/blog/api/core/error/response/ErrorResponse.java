package com.search.blog.api.core.error.response;

import com.search.blog.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(final int status, final String message, final List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this(code.getStatus(), code.getMessage(), errors);
    }

    private ErrorResponse(final ErrorCode code) {
        this(code.getStatus(), code.getMessage(), new ArrayList<>());
    }

    private ErrorResponse(final ErrorCode code, final String message) {
        this(code.getStatus(), message, new ArrayList<>());
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(BusinessException e) {
        return new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = Collections.singletonList(FieldError.of(e.getName(), value, e.getErrorCode()));
        return new ErrorResponse(ErrorCode.BAD_REQUEST, errors);
    }

    public static ErrorResponse of(MissingServletRequestParameterException e) {
        final List<FieldError> errors = Collections.singletonList(FieldError.of(e.getParameterName(), "", "is not present"));
        return new ErrorResponse(ErrorCode.BAD_REQUEST, errors);
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

}
