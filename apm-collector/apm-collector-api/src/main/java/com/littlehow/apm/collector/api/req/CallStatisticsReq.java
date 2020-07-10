package com.littlehow.apm.collector.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel("统计请求接口")
@Data
public class CallStatisticsReq {
    @NotNull(message = "统计类型不可为空")
    @ApiModelProperty("统计类型 1/SYSTEM:系统 2/INTERFACE:接口")
    private String type;

    @NotNull(message = "开始时间不可为空")
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不可为空")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @NotNull(message = "统计top不可为空")
    @ApiModelProperty("统计top")
    private Integer top;

    @ApiModelProperty("系统名称:当统计接口时该值可生效")
    private String serverName;
}
