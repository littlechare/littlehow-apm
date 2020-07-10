package com.littlehow.apm.collector.api.req;

import com.littlehow.apm.base.req.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceLogQueryReq extends PageReq {
    @ApiModelProperty("调用traceId")
    private String traceId;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;
}
