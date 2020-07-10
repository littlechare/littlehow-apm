package com.littlehow.apm.base.req;

import com.littlehow.apm.base.enums.ServerStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 应用信息上报的实体
 * @author littlehow
 */
@Data
@ApiModel(description = "应用请求信息")
public class ApplicationReq {
    @ApiModelProperty("应用名")
    @NotBlank(message = "应用名不可为空")
    private String applicationName;

    @ApiModelProperty("应用中文名")
    private String applicationNameCn;

    @ApiModelProperty("应用端口信息")
    @NotNull(message = "服务端口号不可为空")
    private String serverPort;

    @ApiModelProperty("应用path")
    @NotNull(message = "contextPath不可为空")
    private String contextPath;

    @ApiModelProperty("展示名")
    private String displayName;

    @ApiModelProperty("ip地址")
    @NotBlank(message = "ip地址不可为空")
    private String ip;

    @ApiModelProperty("心跳间隔时间")
    @NotNull(message = "心跳间隔时间")
    private Integer heartbeatDistance;

    @ApiModelProperty("服务状态")
    @NotNull(message = "服务状态不可为空")
    private ServerStatus status;

    @ApiModelProperty("接口信息")
    @NotNull
    @Valid
    private List<InterfaceReq> interfaceReqs;
}
