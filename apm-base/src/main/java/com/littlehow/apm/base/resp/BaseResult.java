package com.littlehow.apm.base.resp;

import lombok.Data;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;

/**
 * 统一返回结果体
 *
 * @author littlehow
 */
@Data
public class BaseResult {
    public static final String SUCCESS_CODE = "000000";
    public static final String SUCCESS_MESSAGE = "success";
    private String code;

    private String message;

    private Object data;

    private String traceId;

    private BaseResult() {
        this.traceId = MDC.get(Span.TRACE_ID_NAME);
    }

    public static BaseResult success(Object data) {
        BaseResult result = new BaseResult();
        result.data = data;
        result.code = SUCCESS_CODE;
        result.message = SUCCESS_MESSAGE;
        return result;
    }

    public static BaseResult success() {
        return success(null);
    }

    public static BaseResult fail(String code, String message) {
        BaseResult result = new BaseResult();
        result.message = message;
        result.code = code;
        return result;
    }

    public static String badGateway() {
        return "sys error traceId=" + MDC.get(Span.TRACE_ID_NAME);
    }
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }
}
