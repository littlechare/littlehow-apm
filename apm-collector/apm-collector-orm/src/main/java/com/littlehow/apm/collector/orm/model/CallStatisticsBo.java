package com.littlehow.apm.collector.orm.model;

import lombok.Data;

/**
 * <p>
 * 调用统计
 * </p>
 *
 * @author littlehow
 * @since 2020-06-09
 */
@Data
public class CallStatisticsBo {

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 上游服务名
     */
    private String parentServerName;

    /**
     * 接口uri
     */
    private String uri;

    /**
     * 统计周期
     */
    private String period;

    /**
     * 周期单位:1:天 2:小时 3:分钟 4:秒
     */
    private Integer periodUnit;

    /**
     * 调用总次数
     */
    private Integer totalCount;

    /**
     * 成功次数
     */
    private Integer successCount;

    /**
     * 失败次数
     */
    private Integer failCount;

}
