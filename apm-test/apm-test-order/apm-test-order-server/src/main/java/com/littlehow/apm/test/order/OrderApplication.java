package com.littlehow.apm.test.order;

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
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.littlehow.apm.test.base",
        "com.littlehow.apm.test.order", "com.littlehow.apm.register.eureka"})
@EnableFeignClients(basePackages = "com.littlehow.apm.test.price.api.client")
public class OrderApplication extends ApmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
