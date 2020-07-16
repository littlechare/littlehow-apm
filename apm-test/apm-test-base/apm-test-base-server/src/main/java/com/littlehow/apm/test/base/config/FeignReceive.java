package com.littlehow.apm.test.base.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class FeignReceive {
    private static final String SUCCESS_RESP_CODE = "000000";
    private static final String SUCCESS_MSG = "success";

    private String code;

    private String message;

    private String data;

    private String traceId;

    public boolean isSuccess() {
        return SUCCESS_RESP_CODE.equals(this.code);
    }
}
