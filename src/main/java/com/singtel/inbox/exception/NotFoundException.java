package com.singtel.inbox.exception;

/**
 * Created by Dongwu on 16/1/2016.
 */
public class NotFoundException extends Exception {
    private static final String PREFIX = "NotFound: ";

    public NotFoundException(String s, Exception e) {
        super(PREFIX + s, e);
    }

    public NotFoundException(String s) {
        super(PREFIX + s);
    }
}
