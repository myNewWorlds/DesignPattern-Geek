package com.ljt.ratelimiter.rule.alg;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;
import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping.UniformRuleConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

@Test
public class UrlRateLimitRuleTest {
    private static final ApiLimit l1 = new ApiLimit("/", 100, 20);
    private static final ApiLimit l2 = new ApiLimit("/user", 90, 10);
    private static final ApiLimit l3 = new ApiLimit("/user/lender", 80, 10);
    private static final ApiLimit l4 = new ApiLimit("/user/borrower/lpd", 70, 10);
    private static final ApiLimit l5 = new ApiLimit("/user/{actorId}/lpd", 60, 10);
    private static final ApiLimit l6 = new ApiLimit("/user/{actorId:(.*)}/lpd", 50, 10);

    public void testAddLimit_and_getLimit() throws InvalidUrlException {
        UrlRateLimitRule rule = new UrlRateLimitRule();
        rule.addLimit("app1", l1);
        rule.addLimit("app1", l2);
        rule.addLimit("app1", l3);

        rule.addLimit("app2", l4);
        rule.addLimit("app2", l5);
        rule.addLimit("app2", l6);

        ApiLimit info = rule.getLimit("app1", "/user");
        assertSame(info, l2);

        info = rule.getLimit("app1", "");
        assertNull(info);

        info = rule.getLimit("app1", null);
        assertNull(info);

        info = rule.getLimit("app1", "/user/borrower/lpd");
        Assert.assertSame(info, l2);

        info = rule.getLimit("app2", "/user/borrower/lpd");
        Assert.assertSame(info, l4);

        info = rule.getLimit("app3", "/user");
        assertNull(info);
    }

    public void testAddLimits() throws InvalidUrlException {
        UrlRateLimitRule rule = new UrlRateLimitRule();
        List<ApiLimit> limits = new ArrayList<>();
        limits.add(l1);
        limits.add(l2);
        limits.add(l3);
        rule.addLimits("app1", limits);

        ApiLimit info = rule.getLimit("app1", "/");
        assertSame(info, l1);
        info = rule.getLimit("app1", "/user");
        assertSame(info, l2);
        info = rule.getLimit("app1", "/user/lender");
        assertSame(info, l3);
    }

    public void testAddRule() throws InvalidUrlException {
        UrlRateLimitRule rule = new UrlRateLimitRule();
        rule.addLimit("app1", l1);
        rule.addLimit("app2", l4);

        UniformRuleConfigMapping ruleConfigMapping = new UniformRuleConfigMapping();
        List<UniformRuleConfig> configs = new ArrayList<>();
        List<ApiLimit> limits1 = Arrays.asList(l2, l3);
        List<ApiLimit> limits2 = Arrays.asList(l5, l6);
        configs.add(new UniformRuleConfig("app1", limits1));
        configs.add(new UniformRuleConfig("app2", limits2));
        ruleConfigMapping.setConfigs(configs);

        rule.addRule(ruleConfigMapping);
        ApiLimit linfo = rule.getLimit("app1", "/");
        assertSame(linfo, l1);
        linfo = rule.getLimit("app1", "/user");
        assertSame(linfo, l2);
        linfo = rule.getLimit("app1", "/user/lender");
        assertSame(linfo, l3);
        linfo = rule.getLimit("app2", "/user/borrower/lpd");
        assertSame(linfo, l4);
    }

    public void testRebuildRule() throws InvalidUrlException {
        UrlRateLimitRule rule = new UrlRateLimitRule();
        rule.addLimit("app1", l1);
        rule.addLimit("app2", l4);

        UniformRuleConfigMapping ruleConfigMapping = new UniformRuleConfigMapping();
        List<UniformRuleConfig> configs = new ArrayList<>();
        List<ApiLimit> limits1 = Arrays.asList(l2, l3);
        List<ApiLimit> limits2 = Arrays.asList(l5, l6);
        configs.add(new UniformRuleConfig("app1", limits1));
        configs.add(new UniformRuleConfig("app2", limits2));
        ruleConfigMapping.setConfigs(configs);
        rule.rebuildRule(ruleConfigMapping);

        ApiLimit linfo = rule.getLimit("app1", "/");
        Assert.assertNull(linfo);
        linfo = rule.getLimit("app1", "/user");
        Assert.assertSame(linfo, l2);
        linfo = rule.getLimit("app1", "/user/lender");
        Assert.assertSame(linfo, l3);
        linfo = rule.getLimit("app2", "/user/borrower/lpd");
        Assert.assertSame(linfo, l6);
    }
}
