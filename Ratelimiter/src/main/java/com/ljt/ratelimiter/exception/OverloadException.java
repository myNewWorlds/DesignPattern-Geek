package com.ljt.ratelimiter.exception;

/**
 * 请求的api超载异常
 */
public class OverloadException extends Exception{

    private static final long serialVersionUID = 270441215863440783L;

    public OverloadException(String message) {
        super(message);
    }

    public OverloadException(String message, Throwable e) {
        super(message, e);
    }
}
