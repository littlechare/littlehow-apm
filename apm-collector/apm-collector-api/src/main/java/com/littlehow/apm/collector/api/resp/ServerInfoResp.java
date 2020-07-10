package com.littlehow.apm.collector.api.resp;

import com.littlehow.apm.base.enums.ServerStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel(description = "服务信息")
@Data
public class ServerInfoResp {
    @ApiModelProperty("服务名称")
    private String serverName;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("端口号")
    private String serverPort;

    @ApiModelProperty("状态")
    private ServerStatus status;

    @ApiModelProperty("心跳间隔时间")
    private Integer heartbeatDistance;

    @ApiModelProperty("最近心跳时间")
    private Long lastHeartbeat;

    @ApiModelProperty("最新上线时间")
    private LocalDateTime lastUpTime;
}
