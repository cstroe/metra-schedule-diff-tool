package com.github.cstroe.metraschedule.parser;

public class TimeParserException extends Exception {
    public TimeParserException(String message) {
        super(message);
    }

    public TimeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
