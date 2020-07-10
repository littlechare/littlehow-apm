package com.littlehow.apm.feign.advice;


import com.littlehow.apm.base.ServerInfo;
import com.littlehow.apm.base.web.RemoteServerContext;
import com.littlehow.apm.base.web.SelfServerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.littlehow.apm.feign.advice.WebAdviceContext.*;

/**
 * 执行feign调用前后的环绕处理器类
 * @author littlehow
 */
@Slf4j
public class AdviceExecutor {

    private static ThreadLocal<Object[]> PRE_PARAM_CACHE = new ThreadLocal<>();

    private static ThreadLocal<String> PRE_SERVICE_NAME = new ThreadLocal<>();

    private static final String SERVICE_NAME_ATTRIBUTE = "SERVICE_NAME_ATTRIBUTE";

    /**
     * 注册processor
     */
    private static List<CallFeignProcessor> processors = new CopyOnWriteArrayList<>();

    /**
     * 增加processor
     * @param processor - 处理器
     */
    public static void addProcessor(CallFeignProcessor processor) {
        processors.add(processor);
    }

    public static void setPreInfo(String serviceName, Object[] params) {
        if (params != null && params.length > 0) {
            PRE_PARAM_CACHE.set(params);
        }
        if (StringUtils.hasText(serviceName)) {
            PRE_SERVICE_NAME.set(serviceName);
        }
    }


    private static Object[] getAndRemoveParam() {
        Object[] params = PRE_PARAM_CACHE.get();
        PRE_PARAM_CACHE.remove();
        return params;
    }

    private static String getAndRemoveServiceName() {
        String serviceName = PRE_SERVICE_NAME.get();
        PRE_SERVICE_NAME.remove();
        return serviceName;
    }

    /**
     * 前置执行
     * @param context  -- 上下文信息
     */
    public static void preExecute(WebAdviceContext context) {
        //设置traceId和spanId, 因为使用sleuth，所以此处必定有值
        context.attribute(TRACE_ID_NAME, MDC.get(TRACE_ID_NAME));
        context.attribute(SPAN_ID_NAME, MDC.get(SPAN_ID_NAME));
        context.attribute(PRE_PARAM, getAndRemoveParam());
        context.attribute(SERVICE_NAME_ATTRIBUTE, getAndRemoveServiceName());
        ServerInfo serverInfo = SelfServerContext.getSelfServerInfo(context.getRequest().url());
        context.setSelf(serverInfo);
        for (CallFeignProcessor processor : processors) {
            try {
                processor.preExecute(context);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("call feign pre execute fail", e);
                }
            }
        }
    }

    /**
     * 后置执行
     * @param context    --  请求信息
     */
    public static void postExecute(WebAdviceContext context) {
        ServerInfo remote = RemoteServerContext.getRemoteServer(context.getRequest().url(), context.attribute(SERVICE_NAME_ATTRIBUTE));
        context.setRemote(remote);
        //及时清除远程host信息
        RemoteServerContext.clearHostPort();
        for (CallFeignProcessor processor : processors) {
            try {
                processor.postExecute(context);
            } catch (Throwable e) {
                if (log.isDebugEnabled()) {
                    log.debug("call feign pre execute fail", e);
                }
            }
        }
    }

}
