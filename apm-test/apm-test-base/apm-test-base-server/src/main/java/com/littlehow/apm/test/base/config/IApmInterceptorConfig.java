package com.littlehow.apm.test.base.config;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 *  拦截器配置
 *
 * @author littlehow
 */
public interface IApmInterceptorConfig {

    /**
     * 获取拦截器
     * @return -
     */
    HandlerInterceptor interceptor();

    /**
     * 需要拦截的
     * @return -
     */
    String[] pathPatterns();

    /**
     * 需要过滤的
     * @return -
     */
    default String[] excludePathPatterns() {
        return new String[0];
    }
}
