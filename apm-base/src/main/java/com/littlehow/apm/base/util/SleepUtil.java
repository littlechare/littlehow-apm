package com.littlehow.apm.base.util;

import java.util.concurrent.TimeUnit;

/**
 * 无中断睡眠工具
 *  拥有5次被打断重新睡眠的机会
 *  中断不会向外层抛出
 * @author littlehow
 */
public class SleepUtil {

    /**
     * 睡眠
     * @param seconds
     */
    public static void sleepSeconds(long seconds) {
        sleepMilliseconds(seconds * 1000);
    }

    /**
     * 毫秒睡眠
     * @param milliseconds -
     */
    public static void sleepMilliseconds(long milliseconds) {
        sleep(milliseconds, System.currentTimeMillis(), 1);
    }

    private static void sleep(long milliseconds, long startTime, int count) {
        try {
            if (count > 5) {
                //5次睡眠被中断则不继续睡眠
                return;
            }
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            //判断是否睡眠到指定时间了
            long current = System.currentTimeMillis();
            long continueTime = current - startTime - milliseconds;
            if (continueTime > 0) {
                sleep(continueTime, current, ++count);
            }
        }
    }
}
