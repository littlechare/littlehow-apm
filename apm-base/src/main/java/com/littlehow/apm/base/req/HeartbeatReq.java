package com.littlehow.apm.base.req;

import com.littlehow.apm.base.enums.ServerStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 心跳上报的实体
 * @author littlehow
 */
@Data
public class HeartbeatReq {
    @ApiModelProperty("应用名")
    @NotBlank(message = "应用名不可为空")
    private String applicationName;

    @ApiModelProperty("应用端口信息")
    @NotNull(message = "服务端口号不可为空")
    private String serverPort;

    @ApiModelProperty("ip地址")
    @NotBlank(message = "ip地址不可为空")
    private String ip;

    @ApiModelProperty("服务状态")
    @NotNull(message = "服务状态不可为空")
    private ServerStatus status;
}
