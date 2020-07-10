package com.littlehow.apm.collector.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.ServerInfo;
import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.collector.biz.ITraceAnalyzeService;
import com.littlehow.apm.collector.biz.enums.StatisticsPeriodUnit;
import com.littlehow.apm.collector.orm.ITraceExecute;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TraceAnalyzeServiceImpl implements ITraceAnalyzeService, InitializingBean {
    private BlockingQueue<TraceInfoReq> queue = new LinkedBlockingQueue<>(1000000);

    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    @Qualifier("mysqlTraceExecute")
    private ITraceExecute traceExecute;

    @Override
    public void addTraceLog(TraceInfoReq req) {
        boolean offer = queue.offer(req);
        if (!offer) {
            log.info("日志队列已满,信息入队失败:{}", JSONObject.toJSONString(req));
        }
    }


    @Override
    public void afterPropertiesSet() {
        //将待处理日志全部取出,初始长度2000
        int initSize = 2000;
        List<TraceInfoReq> traceInfoReqs = new ArrayList<>(initSize);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            int count = queue.drainTo(traceInfoReqs, initSize);
            if (count > 0) {
                log.info("当前处理日志任务条数为:{}", count);
                analyze(traceInfoReqs);
                traceInfoReqs.clear();
            }
        }, 10, 6, TimeUnit.SECONDS);
    }

    /**
     * 分析日志,统计按照分钟进行
     * @param traceInfoReqs -
     */
    private void analyze(List<TraceInfoReq> traceInfoReqs) {
        Map<String, CallStatisticsBo> callStatistics = new HashMap<>();
        Map<String, InterfaceDependenceBo> interfaceDependence = new HashMap<>();
        //统计调用
        traceInfoReqs.forEach(o -> {
            ServerInfo self = JSONObject.parseObject(o.getSelf(), ServerInfo.class);
            ServerInfo remote = JSONObject.parseObject(o.getRemote(), ServerInfo.class);
            //统计
            setStatistics(callStatistics, o, self, remote);
            //分析依赖关系
            setDependence(interfaceDependence, self, remote);
        });
        try {
            traceExecute.saveTraceLogs(traceInfoReqs);
            traceExecute.saveOrUpdateCallStatistics(new ArrayList<>(callStatistics.values()));
            traceExecute.saveOrUpdateDependence(new ArrayList<>(interfaceDependence.values()));
        } catch (Exception e) {
            log.warn("执行日志追踪记录失败", e);
        }

    }

    /**
     * 计算统计
     * @param callStatistics -
     * @param req -
     * @param self -
     * @param remote -
     */
    private void setStatistics(Map<String, CallStatisticsBo> callStatistics, TraceInfoReq req,
                               ServerInfo self, ServerInfo remote) {
        //按分钟进行统计
        String period = StatisticsPeriodUnit.HOUR.getPeriod(req.getStartTime());
        String key = remote.getApplicationName() + "##" + self.getApplicationName() + "##" + req.getRequestUrl() +  "##" + period;
        CallStatisticsBo bo = callStatistics.get(key);
        int success = req.getSuccess() ? 1 : 0;
        if (bo == null) {
            bo = new CallStatisticsBo();
            bo.setServerName(remote.getApplicationName());
            bo.setParentServerName(self.getApplicationName());
            bo.setUri(remote.getServiceName());
            bo.setFailCount(1 - success);
            bo.setSuccessCount(success);
            bo.setTotalCount(1);
            bo.setPeriod(period);
            bo.setPeriodUnit(StatisticsPeriodUnit.HOUR.v);
            callStatistics.put(key, bo);
        } else {
            bo.setFailCount(bo.getFailCount() + 1 - success);
            bo.setSuccessCount(bo.getSuccessCount() + success);
            bo.setTotalCount(bo.getTotalCount() + 1);
        }
    }

    /**
     * 分析依赖关系
     * @param interfaceDependence -
     * @param self -
     * @param remote -
     */
    private void setDependence(Map<String, InterfaceDependenceBo> interfaceDependence, ServerInfo self, ServerInfo remote) {
        String key = self.getApplicationName() + "##" + self.getServiceName() +
                "##" +remote.getApplicationName() + "##" + remote.getServiceName();
        InterfaceDependenceBo bo = interfaceDependence.get(key);
        if (bo == null) {
            bo = new InterfaceDependenceBo();
            bo.setDependenceServerName(remote.getApplicationName());
            bo.setDependenceServerUri(remote.getServiceName());
            bo.setServerName(self.getApplicationName());
            bo.setUri(self.getServiceName());
            bo.setStatus(InterfaceStatus.USING.v);
            interfaceDependence.put(key, bo);
        }
    }
}
