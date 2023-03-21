package com.search.blog.core.exception;

import com.search.blog.core.logger.data.KvData;

/**
 * {@code BusinessAlarmException} 은 개발자에게 알람이 필요한 비지니스 로직의 예외인 경우 통일성 있는 처리를 목표로 함
 * <p>
 * 비지니스 로직에 관련되고, 알람이 필요한 custom exception 을 생성하는 경우
 * 반드시 {@code BusinessAlarmException} 상속하여 구현한다.
 */
public class BusinessAlarmException extends BusinessException {

    public BusinessAlarmException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessAlarmException(String loggerMessage, KvData... datas) {
        super(loggerMessage, datas);
    }

    public BusinessAlarmException(Throwable cause, String loggerMessage, KvData... datas) {
        super(cause, loggerMessage, datas);
    }

    public BusinessAlarmException(String message, String loggerMessage, KvData... datas) {
        super(message, loggerMessage, datas);
    }

}
