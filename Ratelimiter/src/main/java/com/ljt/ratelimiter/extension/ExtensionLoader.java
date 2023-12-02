package com.ljt.ratelimiter.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static final ConcurrentHashMap<Class<?>, Object> CLASS_LOCKS = new ConcurrentHashMap<>();

    //单例模式类Map
    private static final ConcurrentHashMap<Class<?>, List<?>> SINGLETON_OBJECTS = new ConcurrentHashMap<>();

    public static <T> T getExtension(Class<T> clazz) {
        return getExtension(clazz, true);
    }

    public static <T> T getExtension(Class<T> clazz, boolean isSingleton) {
        List<T> extensionList = getExtensionList(clazz, isSingleton);
        if (extensionList == null || extensionList.isEmpty()) {
            return null;
        }
        return extensionList.get(0);
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        return getExtensionList(clazz, true);
    }

    /**
     * 按{@link Order}顺序，得到扩展类
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getExtensionList(Class<T> clazz, boolean isSingleton) {
        if (isSingleton) {
            List<T> extensions = (List<T>) SINGLETON_OBJECTS.get(clazz);
            if (extensions != null && !extensions.isEmpty()) {
                return extensions;
            }
        }

        synchronized (getLoadClassLock(clazz)) {
            //不是单例类，重新加载对象list返回
            if (!isSingleton) {
                return load(clazz);
            }

            //单例类，得到map中对象list
            if (SINGLETON_OBJECTS.containsKey(clazz)) {
                return (List<T>) SINGLETON_OBJECTS.get(clazz);
            }

            List<T> serviceList = load(clazz);
            if (!serviceList.isEmpty()) {
                SINGLETON_OBJECTS.put(clazz, serviceList);
            }
            return serviceList;
        }
    }

    private static <T> List<T> load(Class<T> clazz) {
        List<T> serviceList = new ArrayList<>();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        for (T service : serviceLoader) {
            serviceList.add(service);
        }
        if (!serviceList.isEmpty()) {
            serviceList.sort(OrderComparator.INSTANCE);
        }
        return serviceList;
    }

    /**
     * 得到每个类对应的锁对象
     * 每个对象都有一个锁状态，锁状态被占用时，其他线程就无法访问
     */
    private static <T> Object getLoadClassLock(Class<T> clazz) {
        Object lock = CLASS_LOCKS.get(clazz);
        if (lock == null) {
            Object newLock = new Object();
            lock = CLASS_LOCKS.putIfAbsent(clazz, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }
        return lock;
    }

}
