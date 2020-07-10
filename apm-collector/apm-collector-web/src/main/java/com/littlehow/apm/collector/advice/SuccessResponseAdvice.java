package com.littlehow.apm.collector.advice;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.resp.BaseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author littlehow
 **/
@ControllerAdvice(basePackages = "com.littlehow.apm.collector.controller")
public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

    private static Map<Method, Boolean /* ignore advice */> ignore = new ConcurrentHashMap<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if (ignore.containsKey(method)) {
            return ignore.get(method);
        } else {
            boolean result = true;
            Annotation[] annotations = returnType.getMethodAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof IgnoreAdvice) {
                    result = false;
                    break;
                }
            }
            ignore.put(method, result);
            return result;
        }
    }


    @ResponseBody
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof BaseResult) {
            return body;
        } else if (selectedConverterType == StringHttpMessageConverter.class) {
            response.getHeaders().add("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            return JSONObject.toJSONString(BaseResult.success(body));
        }
        return BaseResult.success(body);

    }

}
