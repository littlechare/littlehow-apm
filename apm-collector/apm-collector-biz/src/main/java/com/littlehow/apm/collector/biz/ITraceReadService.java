package com.littlehow.apm.collector.biz;

import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.resp.PageResult;


public interface ITraceReadService {

    /**
     * 分页查询日志信息
     * @param traceId   --
     * @param pageIndex --
     * @param pageSize  --
     * @return
     */
    PageResult<TraceInfoReq> queryTraceLog(String traceId, Long startTime, Long endTime, int pageIndex, int pageSize);
}
