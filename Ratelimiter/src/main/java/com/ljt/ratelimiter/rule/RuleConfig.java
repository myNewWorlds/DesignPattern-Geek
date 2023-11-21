package com.ljt.ratelimiter.rule;

import lombok.Data;

import java.util.List;

/**
 * 整个yaml文件解析后的结果
 */
@Data
public class RuleConfig {
    private List<AppRuleConfig> configs;

    @Data
    public static class AppRuleConfig {
        private String appId;
        private List<ApiLimit> limits;

        public AppRuleConfig() {
        }

        public AppRuleConfig(String appId, List<ApiLimit> limits) {
            this.appId = appId;
            this.limits = limits;
        }
    }
}
