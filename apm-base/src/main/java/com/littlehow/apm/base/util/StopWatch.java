package com.littlehow.apm.base.util;


import lombok.Getter;

/**
 * 这个stopWatch仅仅作为计时使用
 * @author littlehow
 */
@Getter
public class StopWatch {
    private long startTime;
    private long endTime;

    /**
     * 开始计时
     * @return -
     */
    public StopWatch start() {
        startTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 结束计时
     * @return -
     */
    public StopWatch stop() {
        endTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 时间间隔
     * @return -
     */
    public long during() {
        return endTime - startTime;
    }

    /**
     * 获取计时器
     * @return -
     */
    public static StopWatch get() {
        return new StopWatch();
    }

    /**
     * 获取计时器并开始计时
     * @return -
     */
    public static StopWatch getAndStart() {
        return new StopWatch().start();
    }
}
