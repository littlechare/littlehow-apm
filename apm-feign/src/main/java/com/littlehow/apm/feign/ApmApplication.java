package com.littlehow.apm.feign;

import com.littlehow.apm.base.ApmClassLoader;
import org.springframework.context.annotation.ComponentScan;

/**
 * 使apm收集功能生效，这里不使用starter主要是需要在启动前优先加载一些类
 *
 * @author littlehow
 */
@ComponentScan(basePackages = {"com.littlehow.apm.base", "com.littlehow.apm.feign"})
public class ApmApplication {
    private static ClassLoader loader = new ApmClassLoader();
    static {
        // 加载资源类(主要为了初始化类)
        try {
            // feign实际调用前后的资源类,里面做了前后advice外接处理, 进行类资源注册
            Class.forName("com.littlehow.apm.feign.LoadBalancerFeignClientSource");
            // feign生成代理核心类，对pathVariable进行提前获取，避免同一接口被解释成多个
            // 如/user/{userNo}/info 如果1001和1002调用，分析会出现两个，所以再此处拿出原始path解析
            // 进行类资源注册
            Class.forName("com.littlehow.apm.feign.ReflectiveFeignSource");
            // 对接口注解扫描忽略的类，如接入其他系统的client，也可能使用RequestMapping注解，这样就可能误把其他系统接口解析成自己的接口
            Class.forName("com.littlehow.apm.feign.FeignMappingIgnored");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        //加载类
        try {
            // 优先加载资源类
            loader.loadClass(ReflectiveFeignSource.className);
            loader.loadClass(LoadBalancerFeignClientSource.className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
