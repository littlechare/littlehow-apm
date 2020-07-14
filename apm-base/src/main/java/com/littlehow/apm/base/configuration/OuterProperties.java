package com.littlehow.apm.base.configuration;

import com.littlehow.apm.base.util.IpUtils;
import com.littlehow.apm.base.web.SelfServerContext;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 使用
 * @see ConfigurationProperties
 * 需要配合
 * @see org.springframework.cloud context.environment.EnvironmentChangeEvent
 * 才能实现动态修改
 *
 * @author littlehow
 */
//@ConfigurationProperties("apm")
@Getter
@Configuration
public class OuterProperties implements InitializingBean {

    @Value("${apm.application.name:${spring.application.name:NONE}}")
    private String applicationName;

    @Value("${apm.application.name.cn:}")
    private String applicationNameCn;

    @Value("${apm.application.heartbeat.distance:30}")
    private Integer heartbeatDistance;

    @Value("${apm.application.package.start:}")
    private String packageStart;

    /**
     * 本地服务是否缓存，如果缓存，那么多处调用可能不准确
     */
    @Value("${apm.application.self.server.cache:true}")
    private boolean selfServerCache;

    @Value("${apm.system.timezone.hour:8}")
    private int timezoneHour;

    @Value("${apm.collector.trace.body:true}")
    private boolean traceBody;

    @Value("${apm.collector.use.log:false}")
    private boolean useLog;

    private String collectorUrl;

    @Value("${apm.collector.server.url:}")
    private void setCollectorUrl(String url) {
        if (url.startsWith("http")) {
            collectorUrl = url;
        } else {
            collectorUrl = "";
        }
    }

    @Value("${apm.schedule.pool.size:2}")
    private int schedulePoolSize;

    /**
     * 发送日志、服务信息、心跳的线程池工具
     * 默认核心线程和最大线程为2
     */
    @Value("${apm.thread.pool.size:2}")
    private int threadPoolSize;

    /**
     * 线程池任务队列大小，默认100W
     */
    @Value("${apm.thread.pool.queue.size:1000000}")
    private int threadPoolQueueSize;

    /**
     * 本机ip信息
     */
    private String selfIp;

    /**
     * 系统配置
     */
    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void afterPropertiesSet() {
        SelfServerContext.setProperties(this);
    }

    @Value("${apm.host.ip:}")
    private void setSelfIp(String ip) {
        if (IpUtils.verifyIpv4(ip)) {
            this.selfIp = ip;
        } else {
            this.selfIp = IpUtils.getIp();
        }
    }
}
