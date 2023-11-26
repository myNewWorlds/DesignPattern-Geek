package com.ljt.ratelimiter.rule.source;

import com.ljt.ratelimiter.rule.ApiLimit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 配置文件解析后的统一格式
 */
@Data
public class UniformRuleConfigMapping {
    private List<UniformRuleConfig> configs;

    @Data
    @AllArgsConstructor
    public static class UniformRuleConfig{
        private String appId;
        private List<ApiLimit> limits;

        public UniformRuleConfig() {
        }
    }
}
