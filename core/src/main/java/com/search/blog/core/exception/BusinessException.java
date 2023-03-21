package com.search.blog.core.exception;

import com.search.blog.core.logger.data.Data;
import com.search.blog.core.logger.data.KvData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code BusinessException} 은 비지니스 로직의 예외인 경우 통일성 있는 처리를 목표로 함
 * <p>
 * 비지니스 로직에 관련된 custom exception 을 생성하는 경우
 * 반드시 {@code BusinessException} 상속하여 구현한다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "서비스 오류입니다.";

    private String loggerMessage = "";
    private List<Data> datas = new ArrayList<>();

    public BusinessException() {
        super(DEFAULT_MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String loggerMessage, KvData... datas) {
        super(DEFAULT_MESSAGE);
        this.loggerMessage = loggerMessage;
        this.datas = Arrays.asList(datas);
    }

    public BusinessException(String message, String loggerMessage, KvData... datas) {
        super(message);
        this.loggerMessage = loggerMessage;
        this.datas = Arrays.asList(datas);
    }

    public BusinessException(Throwable cause, String loggerMessage, KvData... datas) {
        super(DEFAULT_MESSAGE, cause);
        this.loggerMessage = loggerMessage;
        this.datas = Arrays.asList(datas);
    }

}
