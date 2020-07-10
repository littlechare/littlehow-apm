package com.littlehow.apm.collector;

import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableSwagger2Doc
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.littlehow.apm"})
@Slf4j
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
        log.info("apm collector run success");
    }
}
