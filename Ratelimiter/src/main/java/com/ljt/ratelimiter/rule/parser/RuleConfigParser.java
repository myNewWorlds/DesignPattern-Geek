package com.ljt.ratelimiter.rule.parser;

import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;

import java.io.InputStream;

/**
 * 配置文件解析器接口
 */
public interface RuleConfigParser {
    UniformRuleConfigMapping parse(String configText);

    UniformRuleConfigMapping  parse(InputStream in);
}
