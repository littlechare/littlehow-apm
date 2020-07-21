package com.littlehow.apm.base.web;

import com.littlehow.apm.base.util.IpUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 根据feign提供分析直连还是非直连
 * @author littlehow
 */
@Slf4j
public class MyApplicationUrl {

    public static String ipPort(String url, String serverName) {
        if (url == null) return null;
        url = url.replace("http://", "").replace("https://", "");
        try {
            while (url.startsWith("/")) {
                url = url.substring(1);
            }
            if (url.contains("/")) {
                url = url.substring(0, url.indexOf("/"));
            }
            if (serverName != null && serverName.equalsIgnoreCase(url) && !url.contains(":")) {
                return null;
            }
            String[] ipPort = url.split(":");
            String ip = getRealIp(ipPort[0]);
            if (ip != null) {
                return ip + (ipPort.length > 1 ? ":" + ipPort[1] : "");
            }
        } catch (Throwable t) {
            log.warn("url=" + url, t);
        }
        return null;
    }

    private static String getRealIp(String ip) {
        if ("localhost".equals(ip)) {
            return IpUtils.getIp();
        }
        if (IpUtils.verifyIpv4(ip)) {
            return ip;
        }
        return null;
    }
}
