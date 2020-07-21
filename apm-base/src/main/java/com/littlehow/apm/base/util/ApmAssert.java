package com.littlehow.apm.base.util;


import com.littlehow.apm.base.exception.ApmAssertException;
import org.springframework.util.StringUtils;

/**
 * 主要是支持值转换和取消爬取异常栈
 * 项目中遇到的断言才加入了
 * @author littlehow
 */
public class ApmAssert {
    public static void isTrue(boolean flag, String message, Object... obj) {
        if (!flag) {
            throw new ApmAssertException(message, obj);
        }
    }

    public static void notNull(Object obj, String message, Object... args) {
        if (obj == null) {
            throw new ApmAssertException(message, args);
        }
    }

    public static void hasText(String str, String message, Object... args) {
        if (!StringUtils.hasText(str)) {
            throw new ApmAssertException(message, args);
        }
    }
}
