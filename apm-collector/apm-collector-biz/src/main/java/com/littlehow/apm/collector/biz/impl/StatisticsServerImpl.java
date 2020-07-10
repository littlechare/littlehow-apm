package com.littlehow.apm.collector.biz.impl;

import com.littlehow.apm.base.exception.ApmAssertException;
import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.collector.biz.IStatisticsService;
import com.littlehow.apm.collector.biz.enums.StatisticsPeriodUnit;
import com.littlehow.apm.collector.biz.enums.StatisticsType;
import com.littlehow.apm.collector.orm.IStatisticsExecute;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.CallStatisticsDetailBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServerImpl implements IStatisticsService {

    @Autowired
    @Qualifier("mysqlStatisticsExecute")
    private IStatisticsExecute statisticsExecute;

    @Override
    public List<CallStatisticsBo> callStatistics(String type, LocalDateTime start, LocalDateTime end, int top, String serverName) {
        String startTime = StatisticsPeriodUnit.HOUR.getPeriod(start);
        String endTime = StatisticsPeriodUnit.HOUR.getPeriod(end);
        if (StatisticsType.isInterface(type)) {
            return statisticsExecute.callInterfaceStatistics(startTime, endTime, top, serverName);
        } else if (StatisticsType.isSystem(type)) {
            return statisticsExecute.callSystemStatistics(startTime, endTime, top);
        }
        throw new ApmAssertException("无效的统计类型:{0}", type);
    }

    @Override
    public List<CallStatisticsDetailBo> callDetailStatistics(String type, LocalDateTime start, LocalDateTime end, String[] serverNames) {
        String startTime = StatisticsPeriodUnit.HOUR.getPeriod(start);
        String endTime = StatisticsPeriodUnit.HOUR.getPeriod(end);
        List<CallStatisticsDetailBo> detailBos = null;
        if (StatisticsType.isInterface(type)) {
            detailBos = statisticsExecute.callInterfaceDetailStatistics(startTime, endTime, serverNames);
        } else if (StatisticsType.isSystem(type)) {
            detailBos = statisticsExecute.callSystemDetailStatistics(startTime, endTime, serverNames);
        }
        ApmAssert.notNull(detailBos, "无效的统计类型:{0}", type);
        Map<String, Integer> map = detailInit(start, end);
        //填充没有数据的点位信息
        detailBos.forEach(o ->
            map.forEach((k, v) -> {
                if (!o.hasPeriod(k)) {
                    o.addTotalCount(k, v);
                    o.addSuccessCount(k, v);
                    o.addFailCount(k, v);
                }
            }));
        return detailBos;
    }

    private static Map<String, Integer> detailInit(LocalDateTime start, LocalDateTime end) {
        Map<String, Integer> map = new HashMap<>();
        while (start.isBefore(end)) {
            map.put(StatisticsPeriodUnit.HOUR.getPeriod(start), 0);
            start = start.plusHours(1);
        }
        //方式开始时间的时分秒不小于结束时间的时分秒而导致最后一小时的数据丢失
        String lastEnd = StatisticsPeriodUnit.HOUR.getPeriod(end);
        map.put(lastEnd, 0);
        return map;
    }

}
