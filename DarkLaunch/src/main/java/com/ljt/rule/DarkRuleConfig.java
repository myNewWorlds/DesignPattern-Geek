package com.ljt.rule;


import lombok.Data;

import java.util.List;

/**
 * 与Yaml文件结构一比一的映射
 * <!--对应DarkRuleConfig-->
 * features:
 * - key: call_newapi_getUserById  <!--对应DarkFeatureConfig-->
 *   enabled: true
 *   rule: {893,342,1020-1120,%30}
 * - key: call_newapi_registerUser <!--对应DarkFeatureConfig-->
 *   enabled: true
 *   rule: {1391198723, %10}
 * - key: newalgo_loan             <!--对应DarkFeatureConfig-->
 *   enabled: true
 *   rule: {0-1000}
 */
@Data
public class DarkRuleConfig {
    private List<DarkFeatureConfig> features;
    @Data
    public static class DarkFeatureConfig{
        private String key;
        private boolean enabled;
        private String rule;
    }
}
