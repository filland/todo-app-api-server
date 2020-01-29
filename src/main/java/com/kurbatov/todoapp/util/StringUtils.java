package com.kurbatov.todoapp.util;

import org.slf4j.helpers.MessageFormatter;

public class StringUtils {

    /**
     * Wrapper for the message formatter from slf4j lib
     * that allows to use {} as placeholders for parameters
     *
     * @param message message with {} placeholders
     * @param params  params to set instead of placeholders
     * @return messages with params instead of placeholders
     */
    public static String formatMessage(String message, String... params) {
        return MessageFormatter.arrayFormat(message, params).getMessage();
    }

}
