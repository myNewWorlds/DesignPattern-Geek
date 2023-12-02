package com.ljt.ratelimiter.env.io;

/**
 * 资源加载器，从指定的位置加载资源
 */
public interface ResourceLoader {
    //类路径url前缀
    String CLASSPATH_URL_PREFIX = "classpath:";

    //文件系统url前缀
    String FILE_URL_PREFIX = "file:";

    //加载指定位置资源
    Resource getResource(String location);
}
