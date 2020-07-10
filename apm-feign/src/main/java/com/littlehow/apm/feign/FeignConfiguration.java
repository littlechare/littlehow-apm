package com.littlehow.apm.feign;

import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.niws.loadbalancer.NIWSDiscoveryPing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * feign配置类
 *
 * @author littlehow
 */
public class FeignConfiguration {

    @Bean
    @Primary
    private RoundRobinRule feignRule() {
        return new FeignRule();
    }

    @Bean
    private NIWSDiscoveryPing selfPing() {
        return new SelfPing();
    }
}
