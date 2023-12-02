package com.ljt.ratelimiter.env.io;

import com.ljt.ratelimiter.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 类路径资源
 *
 */
public class ClassPathResource implements Resource{

    private final String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        String pathToUse = path;
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        this.path = pathToUse;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getExtension() {
        int pos = path.lastIndexOf('.');
        if (pos == -1) {
            return null;
        }
        String extension = path.substring(pos + 1);
        if (StringUtils.isEmpty(extension)) {
            return null;
        }
        return extension;
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = path;
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        builder.append(pathToUse);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean exists() {
        return resolveURL() != null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in;
        if (this.classLoader == null) {
            in = this.classLoader.getResourceAsStream(this.path);
        } else {
            in = ClassLoader.getSystemResourceAsStream(this.path);
        }

        if (in == null) {
            throw new FileNotFoundException( getDescription() + " cannot be opened because it does not exist");
        }
        return in;
    }

    private URL resolveURL() {
        if (this.classLoader != null) {
            return this.classLoader.getResource(this.path);
        } else {
            //从系统类（java的核心类库JDK）路径下加载资源
            return ClassLoader.getSystemResource(this.path);
        }
    }
}
