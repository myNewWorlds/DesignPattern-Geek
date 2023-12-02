package com.ljt.ratelimiter.env.resolver;

import com.ljt.ratelimiter.exception.ConfigurationResolveException;

import java.io.InputStream;
import java.util.Map;

/**
 * 解析资源文件总接口
 * 将文件的内容解析成{@link java.util.Map},
 * 以便在{@link com.ljt.ratelimiter.env.loader.PropertySourceLoader}中转换为{@link com.ljt.ratelimiter.env.PropertySource}
 */
public interface PropertySourceResolver {
    //得到支持解析的文件的所有扩展名
    String[] getSupportedFileExtensions();

    //判断文件扩展名是否支持解析
    boolean canResolvedExtension(String fileExtension);

    //将文件内容的属性配置，解析成Map类型
    Map<String, Object> resolve(InputStream in) throws ConfigurationResolveException;
}
