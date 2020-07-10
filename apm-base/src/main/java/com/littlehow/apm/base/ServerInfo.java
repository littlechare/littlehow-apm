package com.littlehow.apm.base;

import lombok.Data;

/**
 * 服务信息
 * @author littlehow
 */
@Data
public class ServerInfo {
    /**
     * ip地址
     */
    private String ip;

    /**
     * 服务端口号
     */
    private String port;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务说明
     */
    private String serviceExplain;

    /**
     * 类名:self时使用
     */
    private String className;

    /**
     * 方法名:self时使用
     */
    private String methodName;


}
