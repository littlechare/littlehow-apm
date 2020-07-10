package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class DependenceServerNameReq extends ServerNameReq {
    @ApiModelProperty("依赖服务信息")
    @NotBlank(message = "依赖服务信息不可为空")
    private String dependenceServerName;
}
