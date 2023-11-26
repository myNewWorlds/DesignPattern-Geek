package com.ljt.ratelimiter.exception;

public class InvalidUrlException extends Exception {

    private static final long serialVersionUID = 4254430548373613758L;

    public InvalidUrlException(String message) {
        super(message);
    }

    public InvalidUrlException(String message, Throwable e) {
        super(message, e);
    }
}
