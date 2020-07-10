package com.littlehow.apm.feign.advice.support;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.configuration.OuterProperties;
import com.littlehow.apm.base.util.DesensitizationUtil;
import com.littlehow.apm.feign.advice.AbstractCallFeignProcessor;
import com.littlehow.apm.feign.advice.WebAdviceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据上报处理器(可自行实现)
 *
 * @author littlehow
 */
@Slf4j
public class CollectCallFeignProcessor extends AbstractCallFeignProcessor {
    @Autowired
    private OuterProperties outerProperties;

    @Autowired
    private TraceInfoExecutor executor;

    /**
     * 调用前置处理
     * @param context -- 请求上下文
     */
    @Override
    public void preExecute(WebAdviceContext context) {
        //这里不进行任何信息打印，因为除非虚拟机挂掉，否则无论如何都会执行postExecute
        if (log.isDebugEnabled()) {
            log.debug("current request={}, selfServer={}", DesensitizationUtil.desensitizationRequest(context.getRequest().toString()),
                    context.getSelf());
        }
    }

    @Override
    public void postExecute(WebAdviceContext context) {
        boolean traceBody = outerProperties.isTraceBody();
        TraceInfo traceInfo = new TraceInfo(context, traceBody || log.isDebugEnabled());
        if (log.isDebugEnabled()) {
            log.debug("apm traceInfo {}", JSONObject.toJSONString(traceInfo));
        }
        if (!traceBody) {
            traceInfo.clearBody();
        }
        // 日志上报
        executor.addTrace(traceInfo);
    }

}
