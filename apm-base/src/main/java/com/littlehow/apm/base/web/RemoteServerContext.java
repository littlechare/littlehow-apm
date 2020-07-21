package com.littlehow.apm.base.web;

import com.littlehow.apm.base.ServerInfo;

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

    /**
     * 获取远程调用服务信息
     * @param serverName  -- 应用名
     * @param uri         --
     * @return -
     */
    public static ServerInfo getRemoteServer(String serverName, String uri) {
        String[] hostPort = getAndClearHostPort();
        String key = serverName + "##" + uri + "##" + hostPort[0] + "##" + hostPort[1];
        if (REMOTE_SERVER_CACHE.containsKey(key)) {
            return REMOTE_SERVER_CACHE.get(key);
        }
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setApplicationName(serverName);
        serverInfo.setServiceName(uri);
        serverInfo.setIp(hostPort[0]);
        serverInfo.setPort(hostPort[1]);
        REMOTE_SERVER_CACHE.put(key, serverInfo);
        return serverInfo;
    }

    public static void setHostPort(String hostPort) {
        HOST_PORT.set(hostPort);
    }

    private static String[] getAndClearHostPort() {
        String hostPort = HOST_PORT.get();
        HOST_PORT.remove();
        String[] hostPortInfo = new String[2];
        if (hostPort != null) {
            String[] infos = hostPort.split(":");
            hostPortInfo[0] = infos[0];
            if (infos.length > 1) {
                hostPortInfo[1] = infos[1];
            }
        }
        return hostPortInfo;
    }

    public static void clear() {
        HOST_PORT.remove();
    }
}
