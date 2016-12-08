package com.singtel.inbox.exception;

/**
 * Created by Dongwu on 19/1/2016.
 * This exception is thrown whenever an internal error occurs, for example a DAO error if the data store is not accessible.
 * The exception sets a default pattern in the string "INT_ERROR: .*" that can be easily matched from the API Gateway for
 * mapping.
 */
public class InternalErrorException extends Exception {
    private static final String PREFIX = "InternalError: ";

    public InternalErrorException(String s, Exception e) {
        super(PREFIX + s, e);
    }

    public InternalErrorException(String s) {
        super(PREFIX + s);
    }
}