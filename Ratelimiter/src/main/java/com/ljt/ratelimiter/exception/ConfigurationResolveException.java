package com.ljt.ratelimiter.exception;

public class ConfigurationResolveException extends RuntimeException {

    private static final long serialVersionUID = 3161614898854889336L;

    public ConfigurationResolveException(String message) {
        super(message);
    }

    public ConfigurationResolveException(String message, Throwable e) {
        super(message, e);
    }
}
