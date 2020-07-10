package com.littlehow.apm.collector.biz;

import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.CallStatisticsDetailBo;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticsService {

    /**
     * 查询统计信息
     * @param type
     * @param start
     * @param end
     * @param top
     * @param serverName
     * @return
     */
    List<CallStatisticsBo> callStatistics(String type, LocalDateTime start, LocalDateTime end, int top, String serverName);


    /**
     * 查询分时统计信息
     * @param type
     * @param start
     * @param end
     * @param serverNames
     * @return
     */
    List<CallStatisticsDetailBo> callDetailStatistics(String type, LocalDateTime start, LocalDateTime end, String[] serverNames);
}
