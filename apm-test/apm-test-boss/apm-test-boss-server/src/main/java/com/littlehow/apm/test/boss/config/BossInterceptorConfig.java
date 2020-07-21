package com.littlehow.apm.test.boss.config;

import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.test.base.config.IApmInterceptorConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 这里仅仅是模拟登录用户下单
 * 其实boss用户和下单用户往往是不同性质的
 *
 * @author littlehow
 */
@Component
public class BossInterceptorConfig implements IApmInterceptorConfig {
    @Override
    public HandlerInterceptor interceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String userNo = request.getHeader("USER-NO");
                ApmAssert.hasText(userNo, "用户未登录");
                UserContext.setUserNo(userNo);
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
                // do nothing
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                // 清除信息
                UserContext.clear();
            }
        };
    }

    @Override
    public String[] pathPatterns() {
        return new String[] {
                "/apm/test/boss/order/**"
        };
    }
}
