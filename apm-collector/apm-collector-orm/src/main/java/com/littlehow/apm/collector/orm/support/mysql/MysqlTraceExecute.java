package com.littlehow.apm.collector.orm.support.mysql;

import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.orm.ITraceExecute;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.CallStatistics;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.InterfaceDependence;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.TraceLog;
import com.littlehow.apm.collector.orm.support.mysql.service.ICallStatisticsService;
import com.littlehow.apm.collector.orm.support.mysql.service.IInterfaceDependenceService;
import com.littlehow.apm.collector.orm.support.mysql.service.ITraceLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MysqlTraceExecute implements ITraceExecute {
    @Autowired
    protected ITraceLogService traceLogService;

    @Autowired
    protected IInterfaceDependenceService interfaceDependenceService;

    @Autowired
    protected ICallStatisticsService callStatisticsService;

    @Override
    public void saveTraceLogs(List<TraceInfoReq> traceInfoReqs) {
        traceLogService.saveBatch(traceInfoReqs.stream()
                .map(o -> ApmBeanUtils.copyNewOnNull(o, TraceLog.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void saveOrUpdateDependence(List<InterfaceDependenceBo> dependenceBos) {
        List<InterfaceDependence> save = new ArrayList<>();
        dependenceBos.forEach(o -> {
            //查询数据库统计数据
            InterfaceDependence old = interfaceDependenceService.lambdaQuery()
                    .eq(InterfaceDependence::getUri, o.getUri())
                    .eq(InterfaceDependence::getDependenceServerUri, o.getDependenceServerUri())
                    .eq(InterfaceDependence::getDependenceServerName, o.getDependenceServerName())
                    .eq(InterfaceDependence::getServerName, o.getServerName())
                    .one();
            if (old == null) {
                save.add(ApmBeanUtils.copyNewOnNull(o, InterfaceDependence.class));
            }
        });
        if (save.size() > 0) {
            interfaceDependenceService.saveBatch(save);
        }
    }

    @Override
    public void saveOrUpdateCallStatistics(List<CallStatisticsBo> statisticsBos) {
        List<CallStatistics> save = new ArrayList<>();
        List<CallStatistics> update = new ArrayList<>();
        statisticsBos.forEach(o -> {
            CallStatistics old = callStatisticsService.lambdaQuery()
                    .eq(CallStatistics::getServerName, o.getServerName())
                    .eq(CallStatistics::getParentServerName, o.getParentServerName())
                    .eq(CallStatistics::getUri, o.getUri())
                    .eq(CallStatistics::getPeriod, o.getPeriod())
                    .one();
            if (old == null) {
                save.add(ApmBeanUtils.copyNewOnNull(o, CallStatistics.class));
            } else {
                old.setFailCount(old.getFailCount() + o.getFailCount())
                        .setSuccessCount(old.getSuccessCount() + o.getSuccessCount())
                        .setTotalCount(old.getTotalCount() + o.getTotalCount())
                        .setUpdateTime(null);
                update.add(old);
            }
        });
        if (save.size() > 0) {
            callStatisticsService.saveBatch(save);
        }
        if (update.size() > 0) {
            callStatisticsService.updateBatchById(update);
        }

    }
}
