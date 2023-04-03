package com.search.blog.api.external;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

/**
 * http api time out 관련 설정
 */
@Getter
@ToString
public class HttpTimeout {

    private static final Integer DEFAULT_CONNECT_TIMEOUT_MILLIS = 5000;
    private static final Integer DEFAULT_READ_TIMEOUT_SECOND = 5;
    private static final Integer DEFAULT_WRITE_TIMEOUT_SECOND = 5;

    public static final HttpTimeout DEFAULT_HTTP_TIMEOUT = HttpTimeout.of()
            .connectTimeoutMillis(DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .readTimeoutSecond(DEFAULT_READ_TIMEOUT_SECOND)
            .writeTimeoutSecond(DEFAULT_WRITE_TIMEOUT_SECOND)
            .build();

    private final Integer connectTimeoutMillis;
    private final Integer readTimeoutSecond;
    private final Integer writeTimeoutSecond;

    @Builder(builderClassName = "of", builderMethodName = "of", access = PRIVATE)
    private HttpTimeout(@NonNull Integer connectTimeoutMillis,
                        @NonNull Integer readTimeoutSecond,
                        @NonNull Integer writeTimeoutSecond) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutSecond = readTimeoutSecond;
        this.writeTimeoutSecond = writeTimeoutSecond;
    }

}
