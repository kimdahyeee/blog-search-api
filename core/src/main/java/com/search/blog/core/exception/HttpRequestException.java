package com.search.blog.core.exception;

import com.search.blog.core.logger.data.KvData;

public class HttpRequestException extends BusinessAlarmException {

    private static final String DEFAULT_LOGGER_MESSAGE = "api call fail !";

    public HttpRequestException(Exception e) {
        super(DEFAULT_LOGGER_MESSAGE, e);
    }

    public HttpRequestException(KvData... datas) {
        super(DEFAULT_LOGGER_MESSAGE, datas);
    }

    public HttpRequestException(String loggerMessage, KvData... datas) {
        super(loggerMessage, datas);
    }

}
