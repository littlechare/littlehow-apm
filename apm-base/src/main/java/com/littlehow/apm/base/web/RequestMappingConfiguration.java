package com.littlehow.apm.base.web;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * 主要读取自身系统的接口信息
 *
 * @author littlehow
 */
@Configuration
public class RequestMappingConfiguration {

    private static Set<Class<? extends Annotation>> IGNORED = new HashSet<>();

    private static Set<Class<? extends Annotation>> IGNORED_COMPATIBILITY = new HashSet<>();

    static {
        // 当忽略注解和下面注解同时出现时，该接口为自有系统接口
        IGNORED_COMPATIBILITY.add(Controller.class);
        IGNORED_COMPATIBILITY.add(RestController.class);
    }

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new ApmRequestMappingHandlerMapping();
            }
        };
    }

    /**
     * 解析具有RequestMapping和Controller注解的类
     * meta注解也会记录,如RestController, 其注解上游注解Controller
     */
    private static class ApmRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            boolean handler = super.isHandler(beanType) && !hasIgnored(beanType);
            if (handler) {
                Method[] methods = beanType.getMethods();
                for (Method method : methods) {
                    if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    RequestMappingInfo mappingInfo = super.getMappingForMethod(method, beanType);
                    if (mappingInfo != null) {
                        PatternsRequestCondition condition = mappingInfo.getPatternsCondition();
                        Set<String> patterns = condition.getPatterns();
                        for (String uri : patterns) {
                            RequestMappingContext.addMapping(uri, method, beanType);
                        }
                    }
                }
            }
            return handler;
        }

    }

    /**
     * 如果系统自身引入了需要忽略的注解如FeignClient,这样的不需要统计在自身接口中
     *
     * @param beanType -
     * @return
     */
    private static boolean hasIgnored(Class<?> beanType) {
        for (Class<? extends Annotation> clazz : IGNORED) {
            if (AnnotatedElementUtils.hasAnnotation(beanType, clazz)) {
                //判断是否有兼容类型在里面
                for (Class<? extends Annotation> clazz1 : IGNORED_COMPATIBILITY) {
                    if (AnnotatedElementUtils.hasAnnotation(beanType, clazz1)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    public static void registerIgnoredAnnotation(Class<? extends Annotation> clazz) {
        IGNORED.add(clazz);
    }

}
