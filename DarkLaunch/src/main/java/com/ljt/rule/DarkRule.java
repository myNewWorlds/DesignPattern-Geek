package com.ljt.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将{@link DarkRuleConfig} 转换为 {@link DarkFeature}
 */
public class DarkRule {
    //配置文件中加载的灰度规则
    private Map<String, IDarkFeature> darkFeatures = new HashMap<>();
    //编程中的灰度规则
    private final ConcurrentHashMap<String, IDarkFeature> programmedDarkFeatures = new ConcurrentHashMap<>();

    public void addProgrammedDarkFeature(String featureKey, IDarkFeature darkFeature) {
        programmedDarkFeatures.put(featureKey, darkFeature);
    }

    public void setDarkFeatures(Map<String,IDarkFeature> newDarkFeatures) {
        this.darkFeatures = newDarkFeatures;
    }

    public IDarkFeature getDarkFeature(String featureKey) {
        IDarkFeature darkFeature = programmedDarkFeatures.get(featureKey);
        if (darkFeature != null) {
            return darkFeature;
        }
        return darkFeatures.get(featureKey);
    }
}
