package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "服务依赖信息")
public class InterfaceDependenceResp extends ServerDependenceResp {

    @ApiModelProperty("接口uri")
    private String uri;

    @ApiModelProperty("依赖服务uri")
    private String dependenceServerUri;

    @ApiModelProperty("状态 1使用中 2过时")
    private Integer status;
}
