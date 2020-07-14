package com.littlehow.apm.base.web;

import com.littlehow.apm.base.ApplicationInfo;
import com.littlehow.apm.base.ServerInfo;
import com.littlehow.apm.base.configuration.OuterProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本服务上下文
 * @author littlehow
 */
public class SelfServerContext {
    /**
     * 外部配置信息
     */
    private static OuterProperties outerProperties;

    private static ServerProperties serverProperties;

    private static Set<String> PACKAGE_START = new HashSet<>();

    static {
        /**
         * 调用过程可能会有爬栈信息，由于现在系统大部分会依赖外部包以及jdk自有包
         * 所以需要区分真正调用的地方，以自身包名前缀为判断依据
          */
        PACKAGE_START.add("com.littlehow");
    }

    /**
     * 支持外部配置自有系统的报名前缀
     * @see OuterProperties#getPackageStart
     * @param outerProperties -
     */
    public static void setProperties(OuterProperties outerProperties) {
        SelfServerContext.outerProperties = outerProperties;
        serverProperties = outerProperties.getServerProperties();
        String packageStart = outerProperties.getPackageStart();
        if (StringUtils.hasText(packageStart)) {
            PACKAGE_START.addAll(Arrays.asList(packageStart.split(",")));
        }
    }

    /**
     * 本服务信息缓存
     */
    private static Map<String, ServerInfo> SERVER_CACHE = new ConcurrentHashMap<>();


    /**
     * 获取
     * @param url - url
     * @return
     */
    public static ServerInfo getSelfServerInfo(String url) {
        if (outerProperties.isSelfServerCache() && SERVER_CACHE.containsKey(url)) {
            return SERVER_CACHE.get(url);
        }
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setApplicationName(outerProperties.getApplicationName());
        serverInfo.setIp(outerProperties.getSelfIp());
        serverInfo.setPort(serverProperties.getPort() + "");
        setServerInfo(serverInfo);
        if (outerProperties.isSelfServerCache()) {
            SERVER_CACHE.put(url, serverInfo);
        }
        return serverInfo;
    }

    public static ApplicationInfo getApplicationInfo() {
        return new ApplicationInfo(outerProperties, serverProperties);
    }

    /**
     * 进行爬栈分析
     * @param serverInfo -
     */
    private static void setServerInfo(ServerInfo serverInfo) {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        List<String> selfInfo = new ArrayList<>();
        for (StackTraceElement traceElement : traceElements) {
            String className = traceElement.getClassName();
            if (isMine(className)) {
                RequestMappingMeta meta = RequestMappingContext.getRequestMappingMeta(className, traceElement.getMethodName());
                if (meta != null) {
                    setMetaInfo(serverInfo, meta);
                    selfInfo.clear();
                    break;
                } else {
                    selfInfo.add(className + "." + traceElement.getMethodName());
                }
            }
        }
        setPackageInfo(serverInfo, selfInfo);
    }

    private static void setMetaInfo(ServerInfo serverInfo, RequestMappingMeta meta) {
        serverInfo.setClassName(meta.getClassName());
        serverInfo.setMethodName(meta.getMethodName());
        serverInfo.setServiceName(meta.getUri());
        serverInfo.setServiceExplain(meta.getServiceExplain());
    }

    private static void setPackageInfo(ServerInfo serverInfo, List<String> selfInfo) {
        if (selfInfo.size() > 0) {
            String info = selfInfo.get(0);
            int lastIndex = info.lastIndexOf(".");
            serverInfo.setClassName(info.substring(0, lastIndex));
            serverInfo.setMethodName(info.substring(lastIndex + 1));
            serverInfo.setServiceName(serverInfo.getClassName() + "." + serverInfo.getMethodName());
            serverInfo.setServiceExplain("Maybe a timed task");
        }
    }

    private static boolean isMine(String className) {
        for (String start : PACKAGE_START) {
            if (className.startsWith(start)) {
                return true;
            }
        }
        return false;
    }
}
