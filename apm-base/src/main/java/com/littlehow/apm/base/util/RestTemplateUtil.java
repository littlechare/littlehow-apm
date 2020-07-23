package com.littlehow.apm.base.util;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.resp.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * 用于上报日志以及系统信息使用的rest模板工具
 *
 * @author littlehow
 */
@Slf4j
public class RestTemplateUtil {

    private static RestTemplate restTemplate;

    public static void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate = restTemplate;
    }

    /**
     * 无header
     * @param url    --
     * @param param  --
     */
    public static BaseResult post(String url, Object param) {
        if (log.isDebugEnabled()) {
            log.debug("request url={}, param={}", url, JSONObject.toJSONString(param));
        }
        try {
            HttpEntity<Object> entity = new HttpEntity<>(param, getBaseHeader());
            ResponseEntity<BaseResult> result = restTemplate.postForEntity(url, entity, BaseResult.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                return result.getBody();
            }
            log.warn("send remote error {}", result.toString());
        } catch (Exception e) {
            log.warn("send remote error", e);
        }
        return null;
    }

    private static HttpHeaders getBaseHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        String traceId = MDC.get(Span.TRACE_ID_NAME);
        String spanId = MDC.get(Span.SPAN_ID_NAME);
        if (!StringUtils.hasText(traceId)) {
            traceId = Long.toHexString(System.currentTimeMillis());
            spanId = traceId;
            // 可以选择放入MDC，但需要记得调用完结后进行清理
//            MDC.put(Span.TRACE_ID_NAME, traceId);
//            MDC.put(Span.SPAN_ID_NAME, traceId);
        }
        requestHeaders.add(Span.TRACE_ID_NAME, traceId);
        requestHeaders.add(Span.SPAN_ID_NAME, spanId);
        log.info("current request traceId={}, spanId={}", traceId, spanId);
        requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return requestHeaders;
    }
}
