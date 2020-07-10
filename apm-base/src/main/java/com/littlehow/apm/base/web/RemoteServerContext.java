package com.littlehow.apm.base.web;

import com.littlehow.apm.base.ServerInfo;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程服务信息
 * @author littlehow
 */
public class RemoteServerContext {
    private static final ThreadLocal<String> HOST_PORT = new ThreadLocal<>();

    /**
     * 缓存远程服务信息applicationName-host
     */
    private static final Map<String, ServerInfo> REMOTE_SERVER_CACHE = new ConcurrentHashMap<>();

    public static void setHostPort(String hostPort) {
        HOST_PORT.set(hostPort);
    }

    public static String getHostPort() {
        return HOST_PORT.get();
    }

    public static void clearHostPort() {
        HOST_PORT.remove();
    }

    /**
     * 获取远程调用服务信息
     * @param url -
     * @return
     */
    public static ServerInfo getRemoteServer(String url, String serviceName) {
        ServerInfo serverInfo = new ServerInfo();
        MyApplicationUrl myUrl = new MyApplicationUrl(url);
        serverInfo.setApplicationName(myUrl.getApplicationName());
        String hostPort = getHostPort();
        if (!StringUtils.hasText(hostPort)) {
            return serverInfo;
        }
        String key = myUrl.toString() + "-" + hostPort;
        if (REMOTE_SERVER_CACHE.containsKey(key)) {
            return REMOTE_SERVER_CACHE.get(key);
        }
        if (serviceName != null) {
            serverInfo.setServiceName(serviceName);
        } else {
            serverInfo.setServiceName(myUrl.getUri());
        }
        String[] hostPortArr = hostPort.split(":");

        serverInfo.setIp(hostPortArr[0]);
        //无port模式设置空字符串
        serverInfo.setPort(hostPortArr.length > 1 ? hostPortArr[1] : "");
        REMOTE_SERVER_CACHE.put(key, serverInfo);
        return serverInfo;
    }


}
