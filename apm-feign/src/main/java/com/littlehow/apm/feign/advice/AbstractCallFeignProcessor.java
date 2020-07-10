package com.littlehow.apm.feign.advice;

import org.springframework.beans.factory.InitializingBean;

/**
 * 自身类实现该类就会，加上component注解，就会自动注册进入调用前后advice处理器
 *
 * @author littlehow
 */
public abstract class AbstractCallFeignProcessor implements InitializingBean, CallFeignProcessor {

    @Override
    public void afterPropertiesSet() {
        AdviceExecutor.addProcessor(this);
    }
}
