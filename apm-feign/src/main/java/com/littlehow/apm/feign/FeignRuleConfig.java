package com.littlehow.apm.feign;

import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * 将自定义类注册当feign配置中
 * @author littlehow
 */
@RibbonClients(defaultConfiguration = {FeignConfiguration.class})
public class FeignRuleConfig {
}
