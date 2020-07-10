package com.littlehow.apm.feign;

import com.littlehow.apm.base.web.RequestMappingConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 注册网络映射需要忽略的部分
 *
 * @author littlehow
 */
public class FeignMappingIgnored {
    static {
        RequestMappingConfiguration.registerIgnoredAnnotation(FeignClient.class);
    }
}
