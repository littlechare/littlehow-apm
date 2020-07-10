package com.littlehow.apm.feign.advice.support;

import java.util.HashSet;
import java.util.Set;

/**
 * 需要上报的header
 * 可根据自己的微服务系统调用设置
 * @author littlehow
 */
public class CareHeader {

    private static final Set<String> headers = new HashSet<>();

    private static final Set<String> contentTypes = new HashSet<>();

    static {
        // 系统名
        headers.add("SYSTEM");
        // 语言
        headers.add("LANG");

        //目前仅仅支持json
        contentTypes.add("application/json");
    }

    public static boolean isCareHeader(String header) {
        return headers.contains(header.toUpperCase());
    }

    public static boolean isCareContentType(String contentType) {
        for (String s : contentTypes) {
            if (contentType.toLowerCase().startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
