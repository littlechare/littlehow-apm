package com.littlehow.apm.base.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自身接口信息管理上下文
 *
 * @author littlehow
 */
public class RequestMappingContext {
    /**
     * 映射对meta
     */
    private static final Map<String/* uri */, RequestMappingMeta> mappingMataCache = new HashMap<>();

    /**
     * 需要忽略的类或uri
     */
    private static final Set<String> excludeClass = new HashSet<>();

    static {
        // swagger相关api
        excludeClass.add("springfox.documentation.swagger.web.ApiResourceController");
        // springboot内置相关
        excludeClass.add("org.springframework.boot.autoconfigure.web.BasicErrorController");
    }

    /**
     * 方法签名(忽略参数)对meta
     * 如果controller实现是父子关系，并且父类暴露给子类的方法不是拥有RequestMapping注解的方法，需要在此处做兼容缓存
     */
    private static final Map<String/* className.methodName*/, RequestMappingMeta> methodMetaCache = new HashMap<>();

    /**
     * 将request mapping信息缓存起来
     * @param uri     - 对应的uri
     * @param method  - 对应的方法
     * @param target  - 类
     */
    static void addMapping(String uri, Method method, Class<?> target) {
        if (excludeClass.contains(target.getName())) {
            return;
        }
        RequestMappingMeta meta = RequestMappingMeta.getMeta(method, target, uri);
        mappingMataCache.put(uri, meta);
        methodMetaCache.put(meta.getClassName() + "." + meta.getMethodName(), meta);
    }

    /**
     * 根据uri获取meta信息
     * @param uri -
     * @return
     */
    public static RequestMappingMeta getRequestMappingMeta(String uri) {
        return mappingMataCache.get(uri);
    }

    /**
     * 根据类名和方法名获取
     * @param className  - 类名
     * @param methodName - 方法名
     * @return
     */
    public static RequestMappingMeta getRequestMappingMeta(String className, String methodName) {
        String key = className + "." + methodName;
        return methodMetaCache.get(key);
    }


    /**
     * 遍历服务信息
     * @param reader - 读取配置信息
     */
    public static void readAllMapping(RequestMappingReader reader) {
        mappingMataCache.forEach((k, v) -> reader.read(v));
    }
}
