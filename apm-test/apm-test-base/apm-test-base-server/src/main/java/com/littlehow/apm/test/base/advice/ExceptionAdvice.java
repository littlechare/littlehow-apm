package com.littlehow.apm.test.base.advice;

import com.littlehow.apm.base.exception.ApmBizException;
import com.littlehow.apm.test.api.BaseResult;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@ControllerAdvice(value = "com.littlehow.apm.test")
@ResponseBody
@Slf4j
public class ExceptionAdvice {

    private String paramErrorCode = "888888";
    private String systemErrorCode = "999999";
    private String bizErrorCode = "777777";

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult bindExceptionHandler(BindException e) {
        log.info("catch BindException : {}", e.getMessage());
        String retMsg = "";
        if (null != e.getBindingResult() && null != e.getBindingResult().getFieldError()) {
            FieldError fieldError = e.getBindingResult().getFieldError();
            retMsg = fieldError.getDefaultMessage();
        }

        return BaseResult.fail(paramErrorCode, retMsg).setTraceId(getTraceId());
    }

    /**
     * controller入参校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.info("catch MethodArgumentNotValidException : {}, {}, {}", e.getMessage(), e.getParameter(), e.getBindingResult());

        String message = "参数异常";
        BindingResult result = e.getBindingResult();
        if (result.getFieldErrorCount() > 0) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            StringBuilder sb = new StringBuilder();
            fieldErrors.forEach(fe -> sb.append(fe.getDefaultMessage()).append(";"));
            message = sb.substring(0, sb.length() - 1);
        }

        return BaseResult.fail(paramErrorCode, message).setTraceId(getTraceId());
    }

    /**
     * controller参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.info("catch MethodArgumentNotValidException : {}, {}, {}", e.getMessage(), e.getName(), e.getParameter());
        return BaseResult.fail(paramErrorCode, e.getMessage()).setTraceId(getTraceId());
    }

    /**
     * tmpcontroller 方法不允许异常
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public BaseResult methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("catch HttpRequestMethodNotSupportedException : {}, {}, {}", e.getMessage(), e.getMethod(), e.getSupportedHttpMethods());
        return BaseResult.fail(paramErrorCode, e.getMessage()).setTraceId(getTraceId());
    }

    /**
     * tmpcontroller 缺少参数异常
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public BaseResult missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info("catch MissingServletRequestParameterException : {}, {}, {}", e.getMessage(), e.getParameterName(), e.getParameterType());
        return BaseResult.fail(paramErrorCode, e.getMessage()).setTraceId(getTraceId());
    }


    /**
     * 参数类型异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult jsonExceptionHandler(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.warn("catch HttpMessageNotReadableException:{},maybe request data is not valid json", e.getMessage());
        return BaseResult.fail(paramErrorCode, e.getMessage()).setTraceId(getTraceId());
    }


    /**
     * 参数类型异常
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult illegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException e) {
        log.warn("catch IllegalArgumentException exception:{}", e.getMessage(), e);
        return BaseResult.fail(paramErrorCode, e.getMessage()).setTraceId(getTraceId());
    }


    /**
     * 全局业务异常捕获
     */
    @ExceptionHandler(value = {ApmBizException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResult bizExceptionHandler(HttpServletRequest request, ApmBizException e) {
        log.info("catch bizException happened : {}, url : {}", e.getMessage(), request.getRequestURI());
        /**
         * 返回业务异常中的码和说明
         */
        return BaseResult.fail(bizErrorCode, e.getMessage()).setTraceId(getTraceId());

    }

    /**
     * 对于feign client调用失败的时候，把被调用方的异常码和msg返回给前端
     * 在FeignClientRespDecoderConfig中扔出的异常统一会被包装成DecodeException，所以处理这种exception的时候需要抓DecodeException
     * feign.SynchronousMethodHandler.decode(SynchronousMethodHandler.java:169)
     * Object decode(Response response) throws Throwable {
     * try {
     * return decoder.decode(response, metadata.returnType());
     * } catch (FeignException e) {
     * throw e;
     * } catch (RuntimeException e) {
     * throw new DecodeException(e.getMessage(), e);
     * }
     * }
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = DecodeException.class)
    public BaseResult feignDecodeExceptionHandler(HttpServletRequest request, HttpServletResponse resp, Exception e) {
        Throwable rootCause = e.getCause();
        if (rootCause instanceof ApmBizException) {
            ApmBizException bizException = (ApmBizException) rootCause;
            log.info("catch feignDecodeException BizException happened : {}, url : {}", e.getMessage(), request.getRequestURI());
            /**
             * 返回业务异常中的码和说明
             */

            return BaseResult.fail(bizErrorCode, bizException.getMessage());
        } else {
            /**
             * 如果FeignClientRespDecoderConfig返回不是业务异常，那么返回500，
             */
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                resp.getWriter().write(BaseResult.badGateway(getTraceId()));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
            return null;
        }
    }

    /**
     * 如果是扔出自定义的系统异常，返回500
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object bizSystemErrorExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("catch bizSystemErrorException happened, url: {}, exception message : {}", request.getRequestURI(), e.getMessage(), e);
        /**
         * 系统异常返回999999
         */
        return BaseResult.badGateway(getTraceId());
    }


    /**
     * 全局异常捕获       需要返回500
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("catch unexpected exception happened, url: {}, exception message : {}", request.getRequestURI(), e.getMessage(), e);
        /**
         * 系统异常返回999999
         */
        return BaseResult.badGateway(getTraceId());

    }

    private String getTraceId() {
        return MDC.get(Span.TRACE_ID_NAME);
    }
}
