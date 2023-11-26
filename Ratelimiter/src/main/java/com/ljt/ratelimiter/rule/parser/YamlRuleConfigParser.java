package com.ljt.ratelimiter.rule.parser;

import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;
import com.ljt.ratelimiter.utils.YamlUtils;

import java.io.InputStream;

/**
 * yaml配置文件解析器
 */
public class YamlRuleConfigParser implements RuleConfigParser{
    @Override
    public UniformRuleConfigMapping  parse(String configText) {
        return YamlUtils.parse(configText, UniformRuleConfigMapping.class);
    }

    @Override
    public UniformRuleConfigMapping  parse(InputStream in) {
        return YamlUtils.parse(in, UniformRuleConfigMapping .class);
    }
}
