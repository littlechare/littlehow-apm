package com.littlehow.apm.base.web;


import io.swagger.annotations.ApiOperation;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 接口信息类
 *
 * @author littlehow
 */
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
    public static RequestMappingMeta getMeta(Method method, String className, String uri) {
        RequestMappingMeta meta = new RequestMappingMeta();
        meta.className = className;
        meta.methodName = method.getName();
        meta.uri = uri;
        meta.serviceExplain = getExplain(method, uri);
        //获取服务说明
        return meta;
    }

    private static String getExplain(Method method, String uri) {
        //如果有ApiOperation,则使用ApiOperation的说明
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            return apiOperation.value();
        }
        return uri;
    }
}
