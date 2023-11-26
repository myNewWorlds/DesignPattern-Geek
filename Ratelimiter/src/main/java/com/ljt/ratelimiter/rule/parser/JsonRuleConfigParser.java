package com.ljt.ratelimiter.rule.parser;

import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;
import com.ljt.ratelimiter.utils.JsonUtils;

import java.io.InputStream;

public class JsonRuleConfigParser implements RuleConfigParser{
    @Override
    public UniformRuleConfigMapping  parse(String configText) {
        return JsonUtils.json2Object(configText, UniformRuleConfigMapping.class);
    }

    @Override
    public UniformRuleConfigMapping parse(InputStream in) {
        return JsonUtils.stream2Object(in, UniformRuleConfigMapping.class);
    }
}
