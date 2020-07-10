package com.littlehow.apm.base;

import com.littlehow.apm.base.configuration.OuterProperties;
import com.littlehow.apm.base.util.IpUtils;
import lombok.Getter;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * 应用信息
 * @author littlehow
 */
@Getter
public class ApplicationInfo {
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 应用中文名
     */
    private String applicationNameCn;

    /**
     * 应用端口号
     */
    private String serverPort;

    /**
     * 应用根path
     */
    private String contextPath;

    /**
     * 展示名(spring模式值为application)
     */
    private String displayName;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 心跳时间间隔
     */
    private Integer heartbeatDistance;

    public ApplicationInfo(OuterProperties outerProperties, ServerProperties serverProperties) {
        this.applicationName = outerProperties.getApplicationName();
        this.applicationNameCn = outerProperties.getApplicationNameCn();
        this.contextPath = serverProperties.getContextPath();
        if (this.contextPath == null) {
            this.contextPath = "";
        }
        this.ip = IpUtils.getIp();
        this.serverPort = String.valueOf(serverProperties.getPort());
        this.displayName = serverProperties.getDisplayName();
        this.heartbeatDistance = outerProperties.getHeartbeatDistance() * 1000;
    }
}
