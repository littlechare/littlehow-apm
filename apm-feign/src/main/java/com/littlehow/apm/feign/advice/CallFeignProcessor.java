package com.littlehow.apm.feign.advice;

/**
 * feign调用前后处理其
 * 暂时不支持对调用的中断
 * @author littlehow
 */
public interface CallFeignProcessor {
    /**
     * 发送前处理
     * @param context -- 请求上下文
     */
    void preExecute(WebAdviceContext context);

    /**
     * 发送后处理
     * @param context   -- 请求信息
     */
    void postExecute(WebAdviceContext context);
}
