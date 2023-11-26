package com.ljt.ratelimiter.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * SHA1算法
 */
public class SHA1 {
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";

    public static String sha1Hex(String source) {
        return sha1Hex(source, CHARSET_UTF8);
    }

    public static String sha1Hex(String source, String charset) {
        if (!StringUtils.equals(charset, CHARSET_GBK)) {
            charset = CHARSET_UTF8;
        }
        byte[] bSource;
        try {
            bSource = source.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            bSource = source.getBytes();
        }
        return DigestUtils.sha1Hex(bSource);
    }
}
