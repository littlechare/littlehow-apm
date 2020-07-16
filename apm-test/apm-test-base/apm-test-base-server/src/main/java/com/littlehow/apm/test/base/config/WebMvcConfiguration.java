package com.littlehow.apm.test.base.config;

import com.littlehow.apm.base.configuration.OuterProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置跨域以及序列化方式
 * 如果有拦截器，也在此处统一配置即可
 *
 * @author littlehow
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter implements InitializingBean {

    @Autowired
    private OuterProperties outerProperties;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        MyHttpMessageConverter fastConverter = new MyHttpMessageConverter();
        fastConverter.setFastJsonConfig(JsonConfig.getFastJsonConfig());
        converters.add(fastConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void afterPropertiesSet() {
        JsonConfig.setZoneOffset(outerProperties.getTimezoneHour());
    }
}
