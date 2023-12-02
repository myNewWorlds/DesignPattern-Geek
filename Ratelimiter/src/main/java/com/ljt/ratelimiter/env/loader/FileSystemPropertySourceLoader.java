package com.ljt.ratelimiter.env.loader;

import com.ljt.ratelimiter.env.io.ResourceLoader;
import com.ljt.ratelimiter.extension.Order;

/**
 * 文件路径资源，（要传给io的资源）
 */
@Order(Order.HIGHEST_PRECEDENCE + 30)
public class FileSystemPropertySourceLoader extends AbstractFilePropertySourceLoader implements PropertySourceLoader{

    private static final String[] DEFAULT_CONFIG_FILES = new String[] {
            "file:ratelimiter-env.yaml",
            "file:ratelimiter-env.yml",
            "file:ratelimiter-env.properties"
    };

    public FileSystemPropertySourceLoader() {
        this(null);
    }

    public FileSystemPropertySourceLoader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public String[] getAllMatchedConfigFiles() {
        return DEFAULT_CONFIG_FILES;
    }
}
