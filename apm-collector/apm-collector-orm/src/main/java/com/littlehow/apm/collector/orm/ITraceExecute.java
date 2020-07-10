package com.littlehow.apm.collector.orm;



import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.resp.PageResult;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;

import java.util.List;

/**
 * trace执行器
 */
public interface ITraceExecute {
    /**
     * 持久化日志信息
     * @param traceInfoReqs -
     */
    void saveTraceLogs(List<TraceInfoReq> traceInfoReqs);

    /**
     * 持久化依赖关系
     * @param dependenceBos -
     */
    void saveOrUpdateDependence(List<InterfaceDependenceBo> dependenceBos);

    /**
     * 持久化统计信息
     * @param statisticsBos -
     */
    void saveOrUpdateCallStatistics(List<CallStatisticsBo> statisticsBos);

    /**
     * 查询
     * @param traceId -traceId 可为空
     * @return -
     */
    PageResult<TraceInfoReq> queryTraceInfo(String traceId, Long startTime, Long endTime, int pageIndex, int pageSize);
}
