package com.ljt.ratelimiter.env.loader;

import com.ljt.ratelimiter.env.io.ResourceLoader;
import com.ljt.ratelimiter.extension.Order;

/**
 * 类路径下的资源，（要传给io的资源）
 */
@Order(Order.HIGHEST_PRECEDENCE + 40)
public class ClassPathPropertySourceLoader extends AbstractFilePropertySourceLoader implements PropertySourceLoader {

    private static final String[] DEFAUL_CONFIG_FILES = new String[]{
            "classpath:ratelimiter-env.yaml",
            "classpath:ratelimiter-env.yml",
            "classpath:ratelimiter-env.properties"
    };

    public ClassPathPropertySourceLoader() {
        this(null);
    }

    public ClassPathPropertySourceLoader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public String[] getAllMatchedConfigFiles() {
        return DEFAUL_CONFIG_FILES;
    }
}
