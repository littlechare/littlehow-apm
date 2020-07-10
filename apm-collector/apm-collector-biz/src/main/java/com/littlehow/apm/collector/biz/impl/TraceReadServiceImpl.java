package com.littlehow.apm.collector.biz.impl;

import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.resp.PageResult;
import com.littlehow.apm.collector.biz.ITraceReadService;
import com.littlehow.apm.collector.orm.ITraceExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraceReadServiceImpl implements ITraceReadService {

    @Autowired
    @Qualifier("mysqlTraceExecute")
    private ITraceExecute traceExecute;

    @Override
    public PageResult<TraceInfoReq> queryTraceLog(String traceId, Long startTime, Long endTime, int pageIndex, int pageSize) {
        return traceExecute.queryTraceInfo(traceId, startTime, endTime, pageIndex, pageSize);
    }
}
