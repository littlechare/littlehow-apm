package com.littlehow.apm.test.boss;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.littlehow.apm.feign.ApmApplication;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@SpringBootApplication
@EnableSwagger2Doc
@EnableKnife4j
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.littlehow.apm.test.base",
        "com.littlehow.apm.test.boss", "com.littlehow.apm.register.eureka"})
@EnableFeignClients(basePackages = {
        "com.littlehow.apm.test.price.api.client",
        "com.littlehow.apm.test.order.api.client",
        "com.littlehow.apm.test.promote.api.client",
        "com.littlehow.apm.test.user.api.client",
        "com.littlehow.apm.test.shop.api.client"
})
public class BossApplication extends ApmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BossApplication.class, args);
    }
}
