package com.ljt.rule;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.apache.commons.lang3.StringUtils;

public class DarkFeature implements IDarkFeature {
    private final String key;
    private final boolean enabled;
    private int percentage;
    private final RangeSet<Long> rangeSet = TreeRangeSet.create();

    public DarkFeature(DarkRuleConfig.DarkFeatureConfig darkFeatureConfig) {
        this.key = darkFeatureConfig.getKey();
        this.enabled = darkFeatureConfig.isEnabled();
        String darkRule = darkFeatureConfig.getRule();
        if (darkRule == null) {
            return;
        }
        darkRule = darkRule.trim();
        parseDarkRule(darkRule);
    }

    protected void parseDarkRule(String darkRule) {
        //yaml不支持 {}
//        if (!darkRule.startsWith("{") || !darkRule.endsWith("}")) {
//            throw new RuntimeException("Failed to parse dark rule: " + darkRule);
//        }
//        String[] rules = darkRule.substring(1, darkRule.length() - 1).split(",");
        String[] rules = darkRule.split(",");
        this.rangeSet.clear();
        this.percentage = 0;
        for (String rule : rules) {
            rule = rule.trim();
            if (StringUtils.isEmpty(rule)) {
                continue;
            }

            if (rule.startsWith("%")) {
                int newPercentage = Integer.parseInt(rule.substring(1));
                if (newPercentage > this.percentage) {
                    this.percentage = newPercentage;
                }
            } else if (rule.contains("-")) {
                String[] parts = rule.split("-");
                if (parts.length != 2) {
                    throw new RuntimeException("Failed to parse dark rule: " + darkRule);
                }
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                if (start > end) {
                    throw new RuntimeException("Failed to parse dark rule: " + darkRule);
                }
                this.rangeSet.add(Range.closed(start, end));
            } else {
                long val = Long.parseLong(rule);
                this.rangeSet.add(Range.closed(val, val));
            }
        }
    }

    public boolean enabled() {
        return this.enabled;
    }

    /**
     * 判断目标是否再灰度区间
     */
    public boolean dark(long darkTarget) {
        if (this.rangeSet.contains(darkTarget)) {
            return true;
        }
        long reminder = darkTarget % 100;
        if (reminder >= 0 && reminder < this.percentage) {
            return true;
        }
        return false;
    }

    public boolean dark(String darkTarget) {
        long target = Long.parseLong(darkTarget);
        return dark(target);
    }
}
