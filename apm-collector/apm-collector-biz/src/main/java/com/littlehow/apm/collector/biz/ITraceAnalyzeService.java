package com.littlehow.apm.collector.biz;

import com.littlehow.apm.base.req.TraceInfoReq;

public interface ITraceAnalyzeService {

    void addTraceLog(TraceInfoReq req);
}
