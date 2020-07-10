package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class InterfaceNameReq extends ServerNameReq {

    @ApiModelProperty("uri信息")
    @NotBlank(message = "uri信息不可为空")
    private String uri;
}
