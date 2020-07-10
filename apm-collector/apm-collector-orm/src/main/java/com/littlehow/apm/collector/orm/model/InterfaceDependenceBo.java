package com.littlehow.apm.collector.orm.model;

import lombok.Data;

@Data
public class InterfaceDependenceBo {
    /**
     * 服务名
     */
    private String serverName;

    /**
     * 接口uri
     */
    private String uri;

    /**
     * 依赖服务名
     */
    private String dependenceServerName;

    /**
     * 依赖服务uri
     */
    private String dependenceServerUri;

    /**
     * 状态 1使用中 2过时
     */
    private Integer status;
}
