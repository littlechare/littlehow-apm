package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerDependenceResp {
    @ApiModelProperty("服务名")
    private String serverName;

    @ApiModelProperty("依赖服务名")
    private String dependenceServerName;
}
