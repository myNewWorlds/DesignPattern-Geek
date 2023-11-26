package com.ljt.ratelimiter.algorithm;

import com.ljt.ratelimiter.exception.InternalErrorException;

public interface RateLimiter {
    /**
     * try to acquire an acess token
     * 即根据流量限制条件判断当前url能否访问
     * @return true if get an access token successfully, otherwise, return false;
     * @throws InternalErrorException if some internal error occurs
     */
    boolean tryAcquire() throws InternalErrorException;
}
