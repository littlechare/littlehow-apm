package com.littlehow.apm.test.base.config;

import com.littlehow.apm.base.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 调用日志拦截器
 * @author littlehow
 */
@Configuration
@Slf4j
public class LogInterceptorConfig implements IApmInterceptorConfig {

    private static final String STOP_WATCH_KEY = "littlehow-stopwatch";

    @Override
    public HandlerInterceptor interceptor() {
        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                StopWatch sw = StopWatch.getAndStart();
                // 可以考虑解析请求信息
                // log自动会打traceId，所以不需要再打印traceId
                log.info("start---->请求url={}, 请求开始时间:{}", request.getRequestURI(), sw.getStartTime());
                request.setAttribute(STOP_WATCH_KEY, sw);
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
                // do nothing
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                long during = 0;
                StopWatch sw = (StopWatch) request.getAttribute(STOP_WATCH_KEY);
                if (sw != null) {
                    during = sw.stop().during();
                    request.removeAttribute(STOP_WATCH_KEY);
                }
                log.info("end---->请求url={}, 请求结束时间:{}, 请求耗时{}毫秒", request.getRequestURI(),
                        System.currentTimeMillis(), during);
            }
        };
    }

    @Override
    public String[] pathPatterns() {
        return new String[]{"/apm/test/**"};
    }
}
