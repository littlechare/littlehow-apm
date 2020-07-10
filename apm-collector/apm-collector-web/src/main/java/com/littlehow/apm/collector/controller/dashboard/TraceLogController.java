package com.littlehow.apm.collector.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.resp.PageResult;
import com.littlehow.apm.collector.api.req.TraceLogQueryReq;
import com.littlehow.apm.collector.biz.ITraceReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "服务调用日志读取接口")
@RestController
@RequestMapping("/apm/collection/dashboard/log")
@Slf4j
public class TraceLogController {

    @Autowired
    private ITraceReadService traceReadService;

    @ApiOperation("查询日志信息")
    @RequestMapping(value = "queryTraceLog", method = RequestMethod.POST)
    public PageResult<TraceInfoReq> queryTraceLog(@RequestBody @Valid TraceLogQueryReq req) {
        log.info("TraceLogController.queryTraceLog({})", JSONObject.toJSONString(req));
        return traceReadService.queryTraceLog(req.getTraceId(), req.getStartTime(), req.getEndTime(), req.getPageIndex(), req.getPageSize());
    }
}
