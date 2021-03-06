package com.littlehow.apm.test.user;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.littlehow.apm.feign.ApmApplication;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@SpringBootApplication
@EnableSwagger2Doc
@EnableKnife4j
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.littlehow.apm.test.base",
        "com.littlehow.apm.test.user", "com.littlehow.apm.register.eureka"})
public class UserApplication extends ApmApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
