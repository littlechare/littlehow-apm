package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(description = "移除服务")
@Data
public class ServerNameReq {
    @ApiModelProperty("服务名")
    @NotBlank(message = "服务名不可为空")
    private String serverName;
}
