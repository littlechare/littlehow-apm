package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ServerBaseUpdateReq extends ServerNameReq {

    @ApiModelProperty("服务数量")
    @NotNull(message = "服务数量不可为空")
    private Integer serverCount;

    @ApiModelProperty("服务上线数")
    @NotNull(message = "服务上线数量不可为空")
    private Integer serverUp;

    @ApiModelProperty("服务下线数")
    @NotNull(message = "服务下线数量不可为空")
    private Integer serverDown;

    @ApiModelProperty("应用中文名")
    private String serverNameCn;

    @ApiModelProperty("应用path")
    private String contextPath;

    @ApiModelProperty("展示名")
    private String displayName;

}
