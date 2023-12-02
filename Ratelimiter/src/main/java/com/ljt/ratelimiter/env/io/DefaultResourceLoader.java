package com.ljt.ratelimiter.env.io;

import com.ljt.ratelimiter.utils.ClassUtils;

/**
 * 默认资源加载器
 */
public class DefaultResourceLoader implements ResourceLoader{

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
        this(ClassUtils.getDefaultClassLoader());
    }

    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), this.classLoader);
        } else if (location.startsWith(FILE_URL_PREFIX)) {
            //to support URL such as "file:xxxx"
            return new FileSystemResource(location.substring(FILE_URL_PREFIX.length()));
        } else {
            return new FileSystemResource(location);
        }
    }
}
