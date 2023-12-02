package com.ljt.ratelimiter.utils;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            //得到当前线程上下文的类加载器
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (cl == null) {
            //没线程类加载器，则使用当前类的类加载器
            //如果当前类加载器还为null，说明是由jvm的启动类加载器（Bootstrap ClassLoader）加载的
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    //得到系统加载器，系统类加载器是应用程序的默认类加载器，也称为应用程序类加载器（Application ClassLoader）
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {

                }
            }
        }
        return cl;
    }
}
