package com.littlehow.apm.collector.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.collector.biz.ITraceAnalyzeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "服务追踪接口")
@RestController
@RequestMapping("/apm/collection/trace")
@Slf4j
public class TraceController {
    @Autowired
    private ITraceAnalyzeService traceAnalyzeService;

    @ApiOperation("记录日志信息")
    @RequestMapping(value = "log", method = RequestMethod.POST)
    public void log(@RequestBody @Valid TraceInfoReq req) {
        if (log.isDebugEnabled()) {
            log.debug("TraceController.log({})", JSONObject.toJSONString(req));
        }
        traceAnalyzeService.addTraceLog(req);
    }
}
