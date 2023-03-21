package com.search.blog.core.logger.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class KvData implements Data {
    private static final String DELIMITER = "=";

    private final String key;
    private final Object value;

    public static KvData of(String key, Object value) {
        return new KvData(key, value);
    }

    @Override
    public String toString() {
        return key + DELIMITER + value;
    }
}
