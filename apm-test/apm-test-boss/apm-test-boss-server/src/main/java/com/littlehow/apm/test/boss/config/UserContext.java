package com.littlehow.apm.test.boss.config;

/**
 *  模拟用户编号上下文
 *
 * @author littlehow
 */
public class UserContext {
    private static final ThreadLocal<String> userInfo = new ThreadLocal<>();

    public static String getUserNo() {
        return userInfo.get();
    }

    public static void setUserNo(String userNo) {
        userInfo.set(userNo);
    }

    public static void clear() {
        userInfo.remove();
    }
}
