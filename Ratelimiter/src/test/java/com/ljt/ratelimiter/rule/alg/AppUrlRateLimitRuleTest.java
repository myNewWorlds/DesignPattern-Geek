package com.ljt.ratelimiter.rule.alg;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.rule.ApiLimit;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test
public class AppUrlRateLimitRuleTest {
    private static final ApiLimit l1 = new ApiLimit("/", 100, 20);
    private static final ApiLimit l2 = new ApiLimit("/user", 90, 10);
    private static final ApiLimit l3 = new ApiLimit("/user/lender", 80, 10);
    private static final ApiLimit l4 = new ApiLimit("/user/borrower/lpd", 70, 10);
    private static final ApiLimit l5 = new ApiLimit("/user/{username:(^[a-zA-Z]*$)}/lpd", 60, 10);
    private static final ApiLimit l6 = new ApiLimit("/user/{actorId:(^[0-9]*$)}/lpd", 50, 10);
    private static final ApiLimit l7 = new ApiLimit("/wallet/{walletId}", 40, 10);

    @Test
    public void testAddLimitInfo_and_getLimitInfo() throws InvalidUrlException {
        AppUrlRateLimitRule appUrlRateLimitRule = new AppUrlRateLimitRule();
        appUrlRateLimitRule.addLimitInfo(l1);
        appUrlRateLimitRule.addLimitInfo(l2);
        appUrlRateLimitRule.addLimitInfo(l3);
        appUrlRateLimitRule.addLimitInfo(l4);
        appUrlRateLimitRule.addLimitInfo(l5);
        appUrlRateLimitRule.addLimitInfo(l6);
        appUrlRateLimitRule.addLimitInfo(l7);

        ApiLimit actualInfo = appUrlRateLimitRule.getLimitInfo("/");
        //等价于 Assert.assertSame(actualInfo, l1);
        assertEqualsLimitInfo(actualInfo, l1);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user");
        assertEqualsLimitInfo(actualInfo, l2);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/hello");
        assertEqualsLimitInfo(actualInfo, l2);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/lender");
        assertEqualsLimitInfo(actualInfo, l3);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/lender/hello");
        assertEqualsLimitInfo(actualInfo, l3);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/borrower/lpd");
        assertEqualsLimitInfo(actualInfo, l4);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/borrower/lpd/hello");
        assertEqualsLimitInfo(actualInfo, l4);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/borrower/hello");
        assertEqualsLimitInfo(actualInfo, l2);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/zz/lpd");
        assertEqualsLimitInfo(actualInfo, l5);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/12/lpd");
        assertEqualsLimitInfo(actualInfo, l6);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/user/12");
        assertEqualsLimitInfo(actualInfo, l2);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/wallet/88");
        assertEqualsLimitInfo(actualInfo, l7);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/wallet/hello");
        assertEqualsLimitInfo(actualInfo, l1);

        actualInfo = appUrlRateLimitRule.getLimitInfo("/wallet/123/hello");
        assertEqualsLimitInfo(actualInfo, l7);
    }

    public void testAddLimitRule_and_getLimitInfo_withEmptyRule() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        ApiLimit info = rule.getLimitInfo("");
        assertNull(info);
        info = rule.getLimitInfo(null);
        assertNull(info);
        info = rule.getLimitInfo("/");
        assertNull(info);
        info = rule.getLimitInfo("/user/lender");
        assertNull(info);
    }

    public void testAddLimitRule_and_getLimitInfo_ruleOnlyContainsRootPath() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        rule.addLimitInfo(l1);

        ApiLimit actualInfo = rule.getLimitInfo("/");
        assertEqualsLimitInfo(actualInfo, l1);

        actualInfo = rule.getLimitInfo("/user");
        assertEqualsLimitInfo(actualInfo, l1);
    }

    public void testAddLimitRule_withDifferentOrder() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        rule.addLimitInfo(l3);
        rule.addLimitInfo(l2);

        ApiLimit actualInfo = rule.getLimitInfo("/");
        assertNull(actualInfo);

        actualInfo = rule.getLimitInfo("/user");
        assertEqualsLimitInfo(actualInfo, l2);

        actualInfo = rule.getLimitInfo("/user/lender");
        assertEqualsLimitInfo(actualInfo, l3);
    }

    public void testAddLimitRule_withDuplicatedLimitInfos() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        rule.addLimitInfo(l3);
        rule.addLimitInfo(l3);

        ApiLimit actualInfo = rule.getLimitInfo("/user/lender");
        assertEqualsLimitInfo(actualInfo, l3);
    }

    @Test(expectedExceptions = {InvalidUrlException.class})
    public void testAddLimitRule_withInvalidLimitInfo() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        rule.addLimitInfo(new ApiLimit("invalid", 10, 10));
    }

    @Test(expectedExceptions = {InvalidUrlException.class})
    public void testAddLimitRule_withInvalidUrl() throws InvalidUrlException {
        AppUrlRateLimitRule rule = new AppUrlRateLimitRule();
        rule.addLimitInfo(l1);
        rule.addLimitInfo(l2);
        rule.getLimitInfo("invalid url");
    }

    // test thread-safe
    public void testClassThreadSafe() {
    }

    private void assertEqualsLimitInfo(ApiLimit actualInfo, ApiLimit expectedInfo) {
        assertNotNull(actualInfo);
        assertEquals(actualInfo.getApi(), expectedInfo.getApi());
        assertEquals(actualInfo.getLimit(), expectedInfo.getLimit());
    }
}
