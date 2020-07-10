package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "服务接口信息")
public class ServerInterfaceResp {
    @ApiModelProperty("服务名称")
    private String serverName;

    @ApiModelProperty("接口uri")
    private String uri;

    @ApiModelProperty("所在类的类名")
    private String className;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("服务说明")
    private String serviceExplain;

    @ApiModelProperty("rpc标志")
    private Integer rpcFlag;

    @ApiModelProperty("服务接口状态")
    private Integer status;
}
