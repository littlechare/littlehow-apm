package com.littlehow.apm.collector.orm;


import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.CallStatisticsDetailBo;

import java.util.List;

/**
 * 统计接口
 */
public interface IStatisticsExecute {
    /**
     * 统计系统调用信息
     * @param start -- 开始时间
     * @param end   -- 结束时间
     * @param top   -- top值
     * @return
     */
    List<CallStatisticsBo> callSystemStatistics(String start, String end, int top);

    /**
     * 统计接口调用信息
     * @param start -- 开始时间
     * @param end   -- 结束时间
     * @param top   -- top值
     * @param serverName -- 服务名称(可为空)
     * @return
     */
    List<CallStatisticsBo> callInterfaceStatistics(String start, String end, int top, String serverName);


    /**
     * 统计系统分时调用信息
     * @param start        - 开始时间
     * @param end          - 结束时间
     * @param serverNames  - 系统名称
     * @return -
     */
    List<CallStatisticsDetailBo> callSystemDetailStatistics(String start, String end, String[] serverNames);

    /**
     * 统计系统分时调用信息
     * @param start        - 开始时间
     * @param end          - 结束时间
     * @param interfaceNames  - 接口名称
     * @return -
     */
    List<CallStatisticsDetailBo> callInterfaceDetailStatistics(String start, String end, String[] interfaceNames);
}
