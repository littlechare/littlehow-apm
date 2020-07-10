package com.littlehow.apm.feign.advice.support;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.ApplicationInfo;
import com.littlehow.apm.base.configuration.OuterProperties;
import com.littlehow.apm.base.enums.ServerStatus;
import com.littlehow.apm.base.enums.YesOrNo;
import com.littlehow.apm.base.req.ApplicationReq;
import com.littlehow.apm.base.req.HeartbeatReq;
import com.littlehow.apm.base.req.InterfaceReq;
import com.littlehow.apm.base.resp.BaseResult;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.base.util.RestTemplateUtil;
import com.littlehow.apm.base.util.SleepUtil;
import com.littlehow.apm.base.web.RequestMappingContext;
import com.littlehow.apm.base.web.RequestMappingMeta;
import com.littlehow.apm.base.web.SelfServerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 默认监控收集处理类
 * @author littlehow
 */
@Slf4j
public class TraceInfoExecutor implements InitializingBean {

    @Autowired
    @Qualifier("apmRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private OuterProperties outerProperties;

    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    @Qualifier("apmThreadPoolExecutor")
    private ThreadPoolExecutor executor ;

    /**
     * 添加追踪,如果队列已满，则抛弃当前信息
     * @param traceInfo - 追踪信息
     */
    public void addTrace(TraceInfo traceInfo) {
        executor.execute(() -> {
            if (outerProperties.isUseLog()) {
                log.info("apm-collector-trace={}", JSONObject.toJSONString(traceInfo));
            }
            if (StringUtils.hasText(outerProperties.getCollectorUrl())) {
                BaseResult result = RestTemplateUtil.post(outerProperties.getCollectorUrl() + CollectorUri.TRACE_LOG, traceInfo);
                if (result == null || !result.isSuccess()) {
                    log.info("apm-collector-trace-log-error:上传服务信息失败 {}",
                            result == null ? "" : JSONObject.toJSONString(result));
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() {
        RestTemplateUtil.setRestTemplate(restTemplate);
        executor.execute(() -> {
            //延时注册
            SleepUtil.sleepSeconds(10);

            //应用信息
            ApplicationInfo applicationInfo = SelfServerContext.getApplicationInfo();
            //添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (outerProperties.isUseLog()) {
                    log.info("apm-collector-server-shutdown");
                }
                //下线移除服务
                if (StringUtils.hasText(outerProperties.getCollectorUrl())) {
                    RestTemplateUtil.post(outerProperties.getCollectorUrl() + CollectorUri.REMOVE_SERVER,
                            buildHeartbeatReq(applicationInfo));
                }
            }));

            List<RequestMappingMeta> mappings = new ArrayList<>();
            RequestMappingContext.readAllMapping(mappings::add);

            //本机注册服务信息
            if (outerProperties.isUseLog()) {
                log.info("apm-collector-application={}", JSONObject.toJSONString(applicationInfo));
                for (RequestMappingMeta meta : mappings) {
                    log.info("apm-collector-interface={}", JSONObject.toJSONString(meta));
                }

            }
            if (StringUtils.hasText(outerProperties.getCollectorUrl())) {
                BaseResult result = RestTemplateUtil.post(outerProperties.getCollectorUrl() + CollectorUri.UPLOAD_SERVER_INFO,
                        buildReq(applicationInfo, mappings));
                if (result != null && result.isSuccess()) {
                    log.info("apm-collector-interface-remote:上传服务信息成功");
                } else {
                    log.info("apm-collector-interface-remote-error:上传服务信息失败 {}",
                            result == null ? "" : JSONObject.toJSONString(result));
                }

                //构建心跳请求信息
                HeartbeatReq req = buildHeartbeatReq(applicationInfo);
                //发送心跳链接
                scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                            BaseResult r = RestTemplateUtil.post(outerProperties.getCollectorUrl() + CollectorUri.HEARTBEAT_URI, req);
                            if (r == null || !r.isSuccess()) {
                                log.info("apm-collector-heartbeat-error:心跳失败 {}",
                                        r == null ? "" : JSONObject.toJSONString(r));
                            }
                        }, applicationInfo.getHeartbeatDistance().longValue(),
                        applicationInfo.getHeartbeatDistance().longValue(), TimeUnit.MILLISECONDS);
            }
        });
    }

    /**
     * 默认应用为上线
     * 默认接口都支持rpc调用
     * @param applicationInfo -
     * @param mappingMetas -
     * @return
     */
    private ApplicationReq buildReq(ApplicationInfo applicationInfo, List<RequestMappingMeta> mappingMetas) {
        ApplicationReq req = ApmBeanUtils.copyNewOnNull(applicationInfo, ApplicationReq.class);
        // 也可以和eureka状态保持一致
        req.setStatus(ServerStatus.UP);
        req.setInterfaceReqs(mappingMetas.stream().map(o -> {
            InterfaceReq interfaceReq = ApmBeanUtils.copyNewOnNull(o, InterfaceReq.class);
            interfaceReq.setRpcFlag(YesOrNo.YES);
            return interfaceReq;
        }).collect(Collectors.toList()));
        return req;
    }

    /**
     * 构建心跳
     * @param applicationInfo -
     * @return
     */
    private HeartbeatReq buildHeartbeatReq(ApplicationInfo applicationInfo) {
        HeartbeatReq req = new HeartbeatReq();
        req.setApplicationName(applicationInfo.getApplicationName());
        req.setIp(applicationInfo.getIp());
        req.setServerPort(applicationInfo.getServerPort());
        // 这里未来可以和eureka状态保持一致
        req.setStatus(ServerStatus.UP);
        return req;
    }
}
