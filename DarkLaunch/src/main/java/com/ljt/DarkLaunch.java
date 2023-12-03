package com.ljt;

import com.ljt.rule.DarkFeature;
import com.ljt.rule.DarkRule;
import com.ljt.rule.DarkRuleConfig;
import com.ljt.rule.IDarkFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 框架顶层入口类，组装其他对象，串联操作流程
 */
public class DarkLaunch {
    private static final Logger log = LoggerFactory.getLogger(DarkLaunch.class);
    //更新规则的时间间隔
    private static final int DEFAULT_RULE_UPDATE_TIME_INTERVAL = 60;
    private final DarkRule rule = new DarkRule();

    public DarkLaunch() { this(DEFAULT_RULE_UPDATE_TIME_INTERVAL); }

    public DarkLaunch(int ruleUpdateTimeInterval) {
        loadRule();
        //得到定时器
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        //设置定时任务
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                loadRule();
            }
        }, ruleUpdateTimeInterval, ruleUpdateTimeInterval, TimeUnit.SECONDS);
    }

    /**
     * 加载灰度规则
     */
    private void loadRule() {
        //将灰度规则配置文件dark-rule.yaml加载到DarkRuleConfig中
        DarkRuleConfig ruleConfig = null;
        try (InputStream in = this.getClass().getResourceAsStream("/dark-rule.yaml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, DarkRuleConfig.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (ruleConfig == null) {
            throw new RuntimeException("Can not load dark rule.");
        }
        // 修改：单独更新从配置文件中得到的灰度规则，不覆盖编程实现的灰度规则
        Map<String, IDarkFeature> darkFeatures = new HashMap<>();
        for (DarkRuleConfig.DarkFeatureConfig darkFeatureConfig : ruleConfig.getFeatures()) {
            darkFeatures.put(darkFeatureConfig.getKey(), new DarkFeature(darkFeatureConfig));
        }
        this.rule.setDarkFeatures(darkFeatures);
    }

    // 新增：添加编程实现的灰度规则的接口
    public void addProgrammedDarkFeature(String featureKey, IDarkFeature darkFeature) {
        this.rule.addProgrammedDarkFeature(featureKey, darkFeature);
    }

    /**
     * 得到规则优化后的数据结构对象
     */
    public IDarkFeature getDarkFeature(String featureKey) {
        return this.rule.getDarkFeature(featureKey);
    }
}
