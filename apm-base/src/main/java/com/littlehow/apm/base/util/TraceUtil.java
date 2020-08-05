package com.littlehow.apm.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * traceId生成器
 * @author littlehow
 */
public class TraceUtil {

    private static Random random = new Random();

    public static final int TRACE_LENGTH = 16;

    /**
     * 支持单词9位数的随机
     * 操作9位数可以使用多次组合
     */
    private static Map<Integer, Integer> map = new HashMap<>();

    static {
        map.put(1, 10);
        map.put(2, 100);
        map.put(3, 1000);
        map.put(4, 10000);
        map.put(5, 100000);
        map.put(6, 1000000);
        map.put(7, 10000000);
        map.put(8, 100000000);
        map.put(9, 1000000000);
    }

    /**
     * 生成16位数字的traceId
     * @return -
     */
    public static String getTraceId() {
        String time = System.currentTimeMillis() + "";
        int length = time.length();
        if (length > TRACE_LENGTH) {
            time = time.substring(0, TRACE_LENGTH);
        } else if (length < TRACE_LENGTH) {
            time = time + getRandom(TRACE_LENGTH - length);
        }
        return time;
    }

    /**
     * 支持单词9位数的随机
     * 操作9位数可以使用多次组合 (暂时不用实现)
     *
     */
    private static String getRandom(int length) {
        int rand = random.nextInt(map.get(length));
        return String.format("%03d", rand);
    }
}
