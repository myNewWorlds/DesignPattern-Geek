package com.ljt.ratelimiter.env.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource 接口
 */
public interface Resource {
    //得到资源的绝对路径
    String getPath();

    //得到资源的扩展名
    String getExtension();

    //出错时，输出的资源描述
    String getDescription();

    //检查资源是否存在
    boolean exists();

    //得到资源的流
    InputStream getInputStream() throws IOException;
}
