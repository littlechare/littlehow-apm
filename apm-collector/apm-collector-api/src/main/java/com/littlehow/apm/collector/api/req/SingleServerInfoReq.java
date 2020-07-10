package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(description = "移除服务信息")
@Data
public class SingleServerInfoReq {

    @ApiModelProperty("服务名")
    @NotBlank(message = "服务名不可为空")
    private String applicationName;

    @ApiModelProperty("ip地址")
    @NotBlank(message = "ip地址不可为空")
    private String ip;

    @ApiModelProperty("端口号")
    @NotBlank(message = "端口号不可为空")
    private String serverPort;
}
