package com.ljt.ratelimiter.algorithm;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.ljt.ratelimiter.exception.InternalErrorException;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class FixedTimeWindowRateLimiterTest {
    public void testTryAcquire() throws InternalErrorException {
        Ticker ticker = Mockito.mock(Ticker.class);
        //ticker.read() 单位是纳秒
        when(ticker.read()).thenReturn(0L);
        RateLimiter rateLimiter = new FixedTimeWindowRateLimiter(5, Stopwatch.createStarted(ticker));

        when(ticker.read()).thenReturn(100 * 1000 * 1000L);
        boolean passed1 = rateLimiter.tryAcquire();
        assertTrue(passed1);

        when(ticker.read()).thenReturn(200 * 1000 * 1000L);
        boolean passed2 = rateLimiter.tryAcquire();
        assertTrue(passed2);

        when(ticker.read()).thenReturn(300 * 1000 * 1000L);
        boolean passed3 = rateLimiter.tryAcquire();
        assertTrue(passed3);

        when(ticker.read()).thenReturn(400 * 1000 * 1000L);
        boolean passed4 = rateLimiter.tryAcquire();
        assertTrue(passed4);

        when(ticker.read()).thenReturn(500 * 1000 * 1000L);
        boolean passed5 = rateLimiter.tryAcquire();
        assertTrue(passed5);

        when(ticker.read()).thenReturn(600 * 1000 * 1000L);
        boolean passed6 = rateLimiter.tryAcquire();
        assertFalse(passed6);

        when(ticker.read()).thenReturn(1001 * 1000 * 1000L);
        boolean passed7 = rateLimiter.tryAcquire();
        assertTrue(passed7);
    }
}
