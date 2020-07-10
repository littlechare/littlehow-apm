package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "服务基础信息")
public class ServerBaseResp {
    @ApiModelProperty("应用名")
    private String serverName;

    @ApiModelProperty("应用中文名")
    private String serverNameCn;

    @ApiModelProperty("应用path")
    private String contextPath;

    @ApiModelProperty("展示名")
    private String displayName;

    @ApiModelProperty("服务器数量")
    private Integer serverCount;

    @ApiModelProperty("上线数量")
    private Integer serverUp;

    @ApiModelProperty("下线数量")
    private Integer serverDown;

    @ApiModelProperty("服务接口状态 1使用中 2过时")
    private Integer status;
}
