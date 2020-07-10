package com.littlehow.apm.feign.advice.support;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.util.DesensitizationUtil;
import com.littlehow.apm.feign.advice.WebAdviceContext;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上报的日志信息
 *
 * @author littlehow
 */
@Slf4j
@Getter
public class TraceInfo {
    /**
     * sleuth产生的traceId，用户全链路调用追踪
     */
    private String traceId;

    /**
     * 与traceId配套使用
     */
    private String spanId;

    /**
     * 本次请求的URL
     * 如果path是/user/{userNo}/info这样的，在此处会被转换成真实的url如/user/1001/info
     */
    private String requestUrl;

    /**
     * 请求头，只存储关注的header信息
     * @see CareHeader
     */
    private String requestHeaders;

    /**
     * 请求体(会被脱敏)
     * 此处没有对文件上传这样的请求做处理，如果业务需要可以改造此处进行处理
     */
    private String requestBody;

    /**
     * 响应体(会被脱敏)
     */
    private String responseBody;

    /**
     * 调用者自身服务信息
     * @see com.littlehow.apm.base.ServerInfo
     */
    private String self;

    /**
     * 本次调用的远程服务信息
     * @see com.littlehow.apm.base.ServerInfo
     */
    private String remote;

    /**
     * 异常信息
     * 调用出现异常时会有该值
     */
    private String exception;

    /**
     * 是否成功调用，只要不包含都算作是成功
     */
    private Boolean success;

    /**
     * 请求的开始毫秒时间戳
     */
    private Long startTime;

    /**
     * 请求的持续的毫秒时长
     */
    private Long during;

    /**
     * 构建远程调用追踪日志
     * @param context    - 调用上下文
     * @param traceBody  - 是否需要上报请求体或者响应体
     */
    public TraceInfo(WebAdviceContext context, boolean traceBody) {
        this.traceId = context.getTraceId();
        this.spanId = context.getSpanId();
        this.requestUrl = context.getRequest().url();
        this.self = JSONObject.toJSONString(context.getSelf());
        this.remote = JSONObject.toJSONString(context.getRemote());
        if (traceBody) {
            this.requestBody = getRequestBody(context.getRequest(), this.requestUrl, context.getParams());
            if (context.hasResponse()) {
                //判断responseBody是否需要进行追踪
                this.responseBody = getResponseBody(context.getResponse())
                        .replaceAll("\\s+", " ");
                this.responseBody = DesensitizationUtil.desensitizationResponse(this.responseBody);
            }
        }
        this.success = context.hasResponse();
        this.startTime = context.getStartTime();
        this.during = context.getDuring();
        Map<String, String> headers = new HashMap<>();
        context.getRequest().headers().forEach((k, list) -> {
                if (CareHeader.isCareHeader(k)) {
                    headers.put(k, String.join(",", list));
                }
        });
        this.requestHeaders = JSONObject.toJSONString(headers);
        if (context.hasException()) {
            this.exception = getExceptionInfo(context.getException());
        }
        int paramIndex = this.requestUrl.indexOf("?");
        if (paramIndex > 0) {
            this.requestUrl = this.requestUrl.substring(0, paramIndex);
        }
    }

    public void clearBody() {
        this.requestBody = null;
        this.responseBody = null;
    }

    /**
     * 分析异常栈
     * 最多支持10级cause
     * 所以递归调用都应该有调用次数兜底
     * 防止死循环导致栈溢出
     * @param t -
     * @return -
     */
    private String getExceptionInfo(Throwable t) {
        int deep = 10;
        String changeLine = "\n";
        String format = "\t";
        StringBuilder sb  = new StringBuilder();
        while (t != null && deep >= 0) {
            if (deep != 10) sb.append("cause by ");
            sb.append(t.toString()).append(changeLine);
            StackTraceElement[] elements = t.getStackTrace();
            for (StackTraceElement element : elements) {
                sb.append(format).append(element.toString()).append(changeLine);
            }
            t = t.getCause();
            deep--;
        }
        return sb.toString();
    }

    /**
     * 构建请求体
     * @param request  -
     * @param url      -
     * @param params   - 提前拦截的参数(可为空)
     * @return
     */
    private String getRequestBody(Request request, String url, Object[] params) {
        String result = "";
        try {
            // 判断是否有预处理params
            if (params != null && params.length > 0) {
                return requestPreParams(params);
            }
            // 判断是否有request param
            int paramIndex = url.indexOf("?");
            if (paramIndex > 0) {
                result = url.substring(paramIndex + 1);
            }
            if (request.body() != null && request.body().length > 0 && request.charset() != null) {
                // 剔除不必要的空格以及换行
                result = new String(request.body(), request.charset())
                        .replaceAll("\\s+", " ");
                result = DesensitizationUtil.desensitizationRequest(result);
            }
        } catch (Exception e) {
            log.warn("read request error:", e);
        }
        return result;
    }

    /**
     * 响应体
     * @param response -
     * @return
     */
    private String getResponseBody(Response response) {
        String empty = "";
        try {
            Collection<String> headers = response.headers().get("content-type");
            if (CollectionUtils.isEmpty(headers)) {
                return empty;
            } else {
                for (String header : headers) {
                    // 判断是否包含了content-type支持的响应
                    if (CareHeader.isCareContentType(header)) {
                        return Util.toString(response.body().asReader());
                    }
                }
            }
            return empty;
        } catch (Exception e) {
            log.warn("read response error:", e);
        }
        return empty;
    }

    /**
     *  对预处理参数做字符串话处理
     * @param params - 预处理参数
     * @return -
     */
    private String requestPreParams(Object[] params) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : params) {
            if (obj instanceof String
                    || obj instanceof Number
                    || obj instanceof LocalDateTime
                    || obj instanceof LocalDate
                    || obj instanceof LocalTime) {
                sb.append(obj);
            } else if (obj instanceof Date) {
                sb.append(((Date) obj).getTime());
            } else {
                sb.append(DesensitizationUtil.desensitizationRequest(JSONObject.toJSONString(obj)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
