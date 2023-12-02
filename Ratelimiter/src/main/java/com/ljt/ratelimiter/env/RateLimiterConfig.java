package com.ljt.ratelimiter.env;

import com.google.common.annotations.VisibleForTesting;
import com.ljt.ratelimiter.env.loader.ClassPathPropertySourceLoader;
import com.ljt.ratelimiter.env.loader.FileSystemPropertySourceLoader;
import com.ljt.ratelimiter.env.loader.JvmPropertySourceLoader;
import com.ljt.ratelimiter.env.loader.PropertySourceLoader;
import com.ljt.ratelimiter.extension.OrderComparator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 配置总类（单例类）
 * io包标记资源，（传入文件路径，返回资源的流）
 * resolver包根据文件类型进行解析，（传入流，返回Map对象）
 * loader包将前两者进行组合 （先将文件路径传入io包，resolver包解析后，将结果放入PropertySource）
 */
@Data
public class RateLimiterConfig {
    private final List<PropertySourceLoader> sourceLoaders;

    private final RedisConfig redisConfig = new RedisConfig();
    private final ZookeeperConfig zookeeperConfig = new ZookeeperConfig();
    private String ruleConfigParserType = "yaml"; // yaml or json, default yaml
    private String ruleConfigSourceType = "file"; // zookeeper or file, default file

    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static final class RateLimiterConfigHolder{
        private static final RateLimiterConfig INSTANCE = new RateLimiterConfig();
    }
    //为什么要嵌套两层，不懂
    //private static final RateLimiterConfig INSTANCE = new RateLimiterConfig();
    //直接定义为类属性不行吗？
    public static RateLimiterConfig instance() {
        return RateLimiterConfigHolder.INSTANCE;
    }

    public RateLimiterConfig() {
        sourceLoaders = new ArrayList<>();
        sourceLoaders.add(new ClassPathPropertySourceLoader());
        sourceLoaders.add(new FileSystemPropertySourceLoader());
        sourceLoaders.add(new JvmPropertySourceLoader());
        sourceLoaders.add(new JvmPropertySourceLoader());
    }

    @VisibleForTesting
    protected RateLimiterConfig(List<PropertySourceLoader> propertySourceLoaders) {
        this.sourceLoaders = propertySourceLoaders;
    }

    public void load() {
        if (!isInitialized.compareAndSet(false, true)) {
            return;
        }

        PropertySource propertySource = new PropertySource();
        sourceLoaders.sort(OrderComparator.INSTANCE);
        for (PropertySourceLoader loader : sourceLoaders) {
            if (loader != null) {
                propertySource.combinePropertySource(loader.load());
            }
        }
        mapPropertiesToConfigs(propertySource);
    }

    //解决rule配置的源和转换器类型
    private void mapPropertiesToConfigs(PropertySource propertySource) {
        String parseType = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_RULE_CONFIG_SOURCE);
        if (StringUtils.isNotBlank(parseType)) {
            this.ruleConfigParserType = parseType;
        }
        String source = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_RULE_CONFIG_SOURCE);
        if (StringUtils.isNotBlank(source)) {
            this.ruleConfigSourceType = source;
        }
        redisConfig.buildFromProperties(propertySource);
        zookeeperConfig.buildFromProperties(propertySource);
    }
}
