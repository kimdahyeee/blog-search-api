package com.search.blog.core.logger;

import com.search.blog.core.logger.data.Data;

import java.util.Arrays;
import java.util.List;

public class KbLogger {

    private static final String DATA_DELIMITER = " :: ";

    public static String message(String message) {
        return message;
    }

    public static String message(String message, List<Data> datas) {
        return message + DATA_DELIMITER + datas;
    }

    public static String message(String message, Data... datas) {
        return message(message, Arrays.asList(datas));
    }

}
