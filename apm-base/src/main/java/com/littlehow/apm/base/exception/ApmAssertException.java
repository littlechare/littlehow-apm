package com.littlehow.apm.base.exception;

import java.text.MessageFormat;

/**
 * apm业务断言异常类，继承apm业务异常类
 * 不打印调用栈信息(异常不会爬栈)
 * @author littlehow
 */
public class ApmAssertException extends ApmBizException {

    private String message;

    public ApmAssertException(String message, Object ...obj) {
        super(message);
        this.message = convertMessage(message, obj);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return this.getClass().getName() + " : [" + this.getMessage() + "]";
    }

    private String convertMessage(String message, Object[] obj) {
        if (obj == null || obj.length == 0) {
            return message;
        }
        return MessageFormat.format(message, obj);
    }
}
