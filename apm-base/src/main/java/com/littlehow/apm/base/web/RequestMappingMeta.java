package com.littlehow.apm.base.web;


import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 接口信息类
 *
 * @author littlehow
 */
@Slf4j
@Getter
public class RequestMappingMeta {
    /**
     * 接口请求子域名
     */
    private String uri;

    /**
     * 所在类的类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 服务说明
     */
    private String serviceExplain;

    /**
     * 获取meta信息
     * @param method -- 方法名
     * @return - meta
     */
    public static RequestMappingMeta getMeta(Method method, Class<?> targetClass, String uri) {
        RequestMappingMeta meta = new RequestMappingMeta();
        meta.className = targetClass.getName();
        meta.methodName = method.getName();
        meta.uri = uri;
        meta.serviceExplain = getExplain(method, targetClass, uri);
        //获取服务说明
        return meta;
    }

    private static String getExplain(Method method, Class<?> targetClass, String uri) {
        //如果有ApiOperation,则使用ApiOperation的说明
        ApiOperation operation = findOperation(method, targetClass, 1);
        if (operation != null) {
            return operation.value();
        }
        return uri;
    }

    /**
     * 查找是否有api operation
     * @param method      -
     * @param targetClass -
     * @param deep        -
     * @return
     */
    private static ApiOperation findOperation(Method method, Class<?> targetClass, int deep) {
        try {
            // 防止死递归
            if (deep > 20) return null;
            if (method.isAnnotationPresent(ApiOperation.class)) {
                return method.getAnnotation(ApiOperation.class);
            }

            // 优先找接口
            if (targetClass.getInterfaces() != null && targetClass.getInterfaces().length > 0) {
                Class[] classes = targetClass.getInterfaces();
                deep++;
                for (Class<?> c : classes) {
                    // 这里有可能接口并没有次method，将抛出异常
                    try {
                        method = c.getMethod(method.getName(), method.getParameterTypes());
                        ApiOperation operation = findOperation(method, c, deep);
                        if (operation != null) {
                            return operation;
                        }
                    } catch (Throwable t) {
                        // skip
                    }
                }
            }

            // 接口没有找到就找父类
            if (targetClass.getSuperclass() != Object.class) {
                targetClass = targetClass.getSuperclass();
                // 寻找相同的方法
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
                return findOperation(method, targetClass, ++deep);
            }

        } catch (Throwable t) {
            log.warn("find api operation error", t);
        }
        return null;
    }
}
