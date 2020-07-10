package com.littlehow.apm.feign.advice.support;

/**
 * apm收集系统的url，用户服务的注册、日志上报、心跳等
 * @author littlehow
 */
public class CollectorUri {
    //上传服务信息
    public static final String UPLOAD_SERVER_INFO = "apm/collection/application/register";

    //心跳链接
    public static final String HEARTBEAT_URI = "apm/collection/application/heartbeat";

    //移除服务
    public static final String REMOVE_SERVER = "apm/collection/application/removeServerInfo";

    //发送日志
    public static final String TRACE_LOG = "apm/collection/trace/log";
}
