package com.littlehow.apm.test.base.config;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.exception.ApmAssertException;
import com.littlehow.apm.base.exception.ApmBizException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


/**
 *
 * 定制decoder
 * 主要是可以解包同一响应结构体
 * 因为有些场景是同时提供feign给内部和api直接调用
 * 导致结构会被多封装一层FeignReceive格式的响应
 * 也可定制header在统一响应那里进行原样返回
 * 归根结底就是接口定义和响应保持一致结构即可
 * @see feign.SynchronousMethodHandler#decode(Response)
 *
 * 抛出的异常都会被封装为 {@link feign.codec.DecodeException}
 * 所以需要在同一异常处理那里进行进一步处理
 * @see com.littlehow.apm.test.base.advice.ExceptionAdvice#feignDecodeExceptionHandler(HttpServletRequest, HttpServletResponse, Exception)
 *
 * @author littlehow
 */
@Configuration
@Slf4j
public class FeignClientRespDecoderConfig {
    @Bean
    public Decoder createDecoder() {
        return (response, type) -> {
            try {
                HttpStatus httpStatus = HttpStatus.valueOf(response.status());
                if (httpStatus.is2xxSuccessful()) {
                    String feignResp = Util.toString(response.body().asReader());
                    FeignReceive receive = JSONObject.parseObject(feignResp, FeignReceive.class);
                    if (receive.isSuccess()) {
                        String v = receive.getData();
                        if (v == null) {
                            return null;
                        }
                        return convertData(v, type);
                    } else {
                        log.info("feign call is not success, FeignReceive:{}", receive);
                        throw new ApmAssertException(receive.getMessage() + "--> code={0}, traceId={1}", receive.getCode(), receive.getTraceId());
                    }
                } else {
                    log.error("resp http status is not 2xx, HttpStatus:{}", httpStatus);
                    throw new RuntimeException("resp http status is not 2xx, HttpStatus:" + httpStatus);
                }
            } catch (ApmBizException e) {
                throw e;
            } catch (Throwable e1) {
                log.error("feign client fail,error message:{}", e1.getMessage(), e1);
                throw new RuntimeException(e1);
            }

        };
    }

    /**
     * 只处理常用的类型
     *
     * @param data - 数据
     * @param type - 类型
     * @return - 对应类型的对象
     */
    private static Object convertData(String data, Type type) {
        if (void.class == type || Void.class == type || String.class == type) {
            return data;
        } else if (int.class == type || Integer.class == type) {
            return Integer.valueOf(data);
        } else if (long.class == type || Long.class == type) {
            return Long.valueOf(data);
        } else if (BigDecimal.class == type) {
            return new BigDecimal(data);
        } else if (Date.class == type) {
            return new Date(Long.parseLong(data));
        } else if (LocalDateTime.class == type) {
            long milliseconds = Long.parseLong(data);
            return LocalDateTime.ofEpochSecond(milliseconds / 1000, (int) (milliseconds % 1000) * 1000000, JsonConfig.zoneOffset);
        } else if (LocalDate.class == type) {
            long milliseconds = Long.parseLong(data);
            return LocalDate.ofEpochDay(milliseconds / JsonConfig.DAY);
        } else {
            return JSONObject.parseObject(data, type);
        }
    }
}
