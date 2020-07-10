package com.littlehow.apm.base.exception;

/**
 * apm异常类
 * 支持爬栈和不爬栈两种构造方法默认为不爬栈
 *
 * @author littlehow
 */
public class ApmBizException extends RuntimeException {

    public ApmBizException(String msg) {
        this(msg, false);
    }

    public ApmBizException(String msg, boolean writableStack) {
        super(msg, null, true, writableStack);
    }

    public ApmBizException(Throwable cause) {
        super(cause);
    }
}
