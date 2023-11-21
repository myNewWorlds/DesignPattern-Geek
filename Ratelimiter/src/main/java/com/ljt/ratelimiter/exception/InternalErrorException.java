package com.ljt.ratelimiter.exception;

public class InternalErrorException extends Exception {
    private static final long serialVersionUID = 400505098652932743L;

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable e) {
        super(message, e);
    }
}
