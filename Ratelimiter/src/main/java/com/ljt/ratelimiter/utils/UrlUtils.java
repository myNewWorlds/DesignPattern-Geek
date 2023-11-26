package com.ljt.ratelimiter.utils;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utils class for handle url: protocol://host:port/path?query(params)#fragment
 */
public class UrlUtils {
    /**
     * 将URL路径分割成段
     * @param urlPath URL的路径
     * @return 目录路径的list
     * @throws InvalidUrlException url无效错误
     */
    public static List<String> tokenizeUrlPath(String urlPath) throws InvalidUrlException {
        //不返回null，防止调用时空指针异常
        if (StringUtils.isEmpty(urlPath)) {
            return Collections.emptyList();
        }

        if (!urlPath.startsWith("/")) {
            throw new InvalidUrlException("UrlParser tokenize error, invalid urls: " + urlPath);
        }

        String[] dirs = urlPath.split("/");
        List<String> dirList = new ArrayList<>();
        for (String dir : dirs) {
            //含有. ? * 等特殊字符的时候保证在 {} 之间，（URL模板的标准语法）
            if ((dir.contains(".") || dir.contains("?") || dir.contains("*"))
                    && (!dir.startsWith("{") || !dir.endsWith("}"))) {
                throw new InvalidUrlException("UrlParser tokenize error, invalid urls: " + urlPath);
            }
            if (!StringUtils.isEmpty(dir)) {
                dirList.add(dir);
            }
        }
        return dirList;
    }

    /**
     * 得到url的路径，移除参数
     * <p>{@literal "http://www.test.com/v1/user" -> "/v1/user" }</p>
     * <p>{@literal "/v1/user" -> "/v1/user" }</p>
     * <p>{@literal "/v1/user?lender=true" --> "/v1/user"}</p>
     */
    public static String getUrlPath(String url) throws InvalidUrlException {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        URI urlObj;
        try {
            urlObj = new URI(url);
        } catch (URISyntaxException e) {
            throw new InvalidUrlException("Get url path error:" + url, e);
        }

        String path = urlObj.getPath();
        if (StringUtils.isEmpty(path)) {
            return "/";
        }
        return path;
    }

    /**
     * 判断是否为URL
     */
    public static boolean validUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
