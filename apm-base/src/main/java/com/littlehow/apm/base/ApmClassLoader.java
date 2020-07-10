package com.littlehow.apm.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 字节资源类加载器
 * 使用父加载器来定义加载类
 * 因为此加载器加载的类都是重写三方类的资源
 * 不交给父加载器进行定义的话，父加载器还会继续从资源寻找该类
 * 而因为jar的加载顺序不可控，所以可能加载在不是自己修改后的类(同包同名)
 * 如org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient
 * @author littlehow
 */
public class ApmClassLoader extends ClassLoader {

    /**
     * classloader中的保护方法，需要使用反射调用
     */
    private static Method DEFINE_CLASS_METHOD ;

    /**
     * 当前类加载器已经加载过的类
     */
    private static final Map<String, Class<?>> loadedClassMap = new HashMap<>();

    /**
     * 加载ApmClassLoader的类加载器
     */
    private static ClassLoader parent;
    static {
        Method[] methods = ClassLoader.class.getDeclaredMethods();
        for (Method method : methods) {
            if ("defineClass".equals(method.getName()) && method.getParameterCount() == 4) {
                DEFINE_CLASS_METHOD = method;
                method.setAccessible(true);
            }
        }
        parent = ApmClassLoader.class.getClassLoader();
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        synchronized (this) {
            Class<?> c = loadedClassMap.get(className);
            if (c == null) {
                c = findClass(className);
                loadedClassMap.put(className, c);
            }
            return c;
        }
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        // 加载后就不再保存
        byte[] bts = ApmClassSourceManager.classes.remove(className);
        if (bts == null || bts.length == 0) {
            // 如果没有资源类，则调用parent加载器进行加载
            return parent.loadClass(className);
        }
        // 定义给父加载器，保证父类不再去自行加载
        Class<?> clazz = executeDefine(parent, className, bts);
        if (clazz == null) {
            throw new ClassNotFoundException(className);
        }
        return clazz;
    }

    /**
     * 加载类
     * 普通项目的类加载器为应用类加载器:
     * @see sun.misc.Launcher $AppClassLoader
     * 但是当项目被打成springboot的jar包后，加载该类的类加载器是
     * @see org.springframework.boot loader.LaunchedURLClassLoader
     * 该类加载器放置于线程上下文类加载器中
     *
     * @param loader    -  加载器
     * @param className -  类名
     * @param bts       -  字节码
     * @return - 类名对应的class对象
     */
    private Class<?> executeDefine(ClassLoader loader, String className, byte[] bts) {
        try {
            return (Class<?>) DEFINE_CLASS_METHOD.invoke(loader, className, bts, 0, bts.length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
