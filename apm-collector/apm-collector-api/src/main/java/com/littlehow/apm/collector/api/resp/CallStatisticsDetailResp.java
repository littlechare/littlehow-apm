package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CallStatisticsDetailResp {
    @ApiModelProperty("服务或接口名称")
    private String serverName;

    @ApiModelProperty("接口名称")
    private String uri;

    @ApiModelProperty("详细调用量信息")
    Map<String, Integer> totalCount;

    @ApiModelProperty("成功调用信息")
    Map<String, Integer> successCount;

    @ApiModelProperty("失败调用信息")
    Map<String, Integer> failCount;
}
