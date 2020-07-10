package com.littlehow.apm.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 远程调用日志上报结构信息
 * @author littlehow
 */
@Data
@ApiModel(description = "日志信息")
public class TraceInfoReq {
    @ApiModelProperty("追踪编号")
    @NotBlank(message = "traceId不可为空")
    private String traceId;

    @ApiModelProperty("spanId")
    private String spanId;

    @ApiModelProperty("请求url")
    private String requestUrl;

    @ApiModelProperty("请求头")
    private String requestHeaders;

    @ApiModelProperty("请求体")
    private String requestBody;

    @ApiModelProperty("响应体")
    private String responseBody;

    @ApiModelProperty("自身服务信息")
    private String self;

    @ApiModelProperty("远程服务信息")
    private String remote;

    @ApiModelProperty("异常信息")
    private String exception;

    @ApiModelProperty("成功标志")
    private Boolean success;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("持续时间")
    private Long during;

}
