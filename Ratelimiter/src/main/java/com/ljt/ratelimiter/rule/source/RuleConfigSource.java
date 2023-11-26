package com.ljt.ratelimiter.rule.source;

/**
 * 不同种类数据源加载配置规则
 */
public interface RuleConfigSource {
    //所有规则最终都要加载成 UniformRuleConfigMapping 类
    UniformRuleConfigMapping load();
}
