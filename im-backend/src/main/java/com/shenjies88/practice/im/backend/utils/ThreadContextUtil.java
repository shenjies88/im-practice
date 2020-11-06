package com.shenjies88.practice.im.backend.utils;

/**
 * @author shenjies88
 * @since 2020/11/6-8:57 AM
 */
public class ThreadContextUtil {

    private static final ThreadLocal context = new ThreadLocal<Object>();

    public static void set(Object o) {
        context.set(o);
    }

    public static Object get() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}
