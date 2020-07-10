package com.littlehow.apm.feign;

import com.littlehow.apm.feign.advice.CallFeignProcessor;
import com.littlehow.apm.feign.advice.support.CollectCallFeignProcessor;
import com.littlehow.apm.feign.advice.support.TraceInfoExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * apm上报信息类配置
 * 可以自己实现信息上报
 * @author littlehow
 */
@Configuration
public class ApmFeignConfiguration {

    @Bean
    @ConditionalOnProperty(value = "apm.collector.enable", matchIfMissing = true)
    public CallFeignProcessor createCollectProcessor() {
        return new CollectCallFeignProcessor();
    }

    @Bean
    @DependsOn("outerProperties")
    public TraceInfoExecutor createTraceInfoExecutor() {
        return new TraceInfoExecutor();
    }
}
