package com.littlehow.apm.test.base.config;

import com.littlehow.apm.base.configuration.OuterProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 配置跨域以及序列化方式
 * 如果有拦截器,参考 {@link IApmInterceptorConfig} 配置
 *
 * @author littlehow
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter implements InitializingBean {

    @Autowired
    private OuterProperties outerProperties;

    @Autowired
    private Map<String, IApmInterceptorConfig> interceptorConfigMap;

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

    /**
     * 客户端配置拦截器
     * @param registry -
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!CollectionUtils.isEmpty(interceptorConfigMap)) {
            interceptorConfigMap.forEach((k, config) ->
                registry.addInterceptor(config.interceptor()).addPathPatterns(config.pathPatterns())
                        .excludePathPatterns(config.excludePathPatterns())
            );
            super.addInterceptors(registry);
        }
    }

    @Override
    public void afterPropertiesSet() {
        JsonConfig.setZoneOffset(outerProperties.getTimezoneHour());
    }
}
