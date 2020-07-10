package com.littlehow.apm.feign.advice;

import com.littlehow.apm.base.ServerInfo;
import feign.Request;
import feign.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * feign调用上下文信息
 * @author littlehow
 */
@Getter
@Setter
public class WebAdviceContext {

    public static final String TRACE_ID_NAME = "X-B3-TraceId";
    public static final String SPAN_ID_NAME = "X-B3-SpanId";
    public static final String PRE_PARAM = "PRE-PARAM";
    /**
     * 请求信息
     */
    private Request request;

    /**
     * 响应信息
     */
    private Response response;

    /**
     * 本机服务信息
     */
    private ServerInfo self;

    /**
     * 远程服务信息
     */
    private ServerInfo remote;

    /**
     * 执行开始时间
     */
    private long startTime;

    /**
     * 执行信息
     */
    private long during;

    /**
     * 客户端自定义属性
     */
    private Map<String, Object> attributes;

    /**
     * 异常
     */
    private Throwable exception;

    public boolean hasException() {
        return exception != null;
    }

    public boolean hasResponse() {
        return response != null;
    }

    public void attribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    /**
     * 可能会出现类型转换异常
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T attribute(String key) {
        if (attributes != null) {
            return (T) attributes.get(key);
        }
        return null;
    }

    /**
     * 获取traceId
     * @return
     */
    public String getTraceId() {
        return attribute(TRACE_ID_NAME);
    }

    public String getSpanId() {
        return attribute(SPAN_ID_NAME);
    }

    /**
     * 获取预存参数
     * @return -
     */
    public Object[] getParams() {
        return attribute(PRE_PARAM);
    }

    public static WebAdviceContext getInstance(Request request, long startTime) {
        WebAdviceContext context = new WebAdviceContext();
        context.request = request;
        context.startTime = startTime;
        return context;
    }
}
