package com.littlehow.apm.collector.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "调用统计信息")
public class CallStatisticsResp {

    @ApiModelProperty("服务或接口名称")
    private String serverName;

    @ApiModelProperty("服务名称")
    private String uri;

    @ApiModelProperty("总调用数")
    private Integer totalCount;

    @ApiModelProperty("总调用成功数")
    private Integer successCount;

    @ApiModelProperty("总调用失败数")
    private Integer failCount;
}
