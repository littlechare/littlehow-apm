package com.littlehow.apm.collector.orm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class CallStatisticsDetailBo {
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

    public void addTotalCount(String key, Integer value) {
        if (totalCount == null) {
            totalCount = new TreeMap<>();
        }
        totalCount.put(key, value);
    }

    public void addSuccessCount(String key, Integer value) {
        if (successCount == null) {
            successCount = new TreeMap<>();
        }
        successCount.put(key, value);
    }

    public void addFailCount(String key, Integer value) {
        if (failCount == null) {
            failCount = new TreeMap<>();
        }
        failCount.put(key, value);
    }

    public boolean hasPeriod(String key) {
        return totalCount != null && totalCount.containsKey(key);
    }
}
