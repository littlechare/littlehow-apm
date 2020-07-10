package com.littlehow.apm.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 二进制class资源缓存类,配合apm类加载器使用
 * @see ApmClassLoader
 *
 * @author littlehow
 */
public class ApmClassSourceManager {
    static Map<String, byte[]> classes = new HashMap<>();

    /**
     * 将需要加载的类注册进来
     * @param className  - 类名
     * @param code       - 对应的二进制
     */
    public static void register(String className, byte[] code) {
        classes.put(className, code);
    }
}
