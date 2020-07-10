package com.littlehow.apm.collector.orm.support.mysql;

import com.littlehow.apm.collector.orm.IStatisticsExecute;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.CallStatisticsDetailBo;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ext.CallStatisticsExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("mysqlStatisticsExecute")
public class MysqlStatisticsExecute implements IStatisticsExecute {

    @Autowired
    private CallStatisticsExtMapper callStatisticsExtMapper;

    @Override
    public List<CallStatisticsBo> callSystemStatistics(String start, String end, int top) {
        return callStatisticsExtMapper.selectCallSystemStatistics(start, end, top);
    }

    @Override
    public List<CallStatisticsBo> callInterfaceStatistics(String start, String end, int top, String serverName) {
        return callStatisticsExtMapper.selectCallInterfaceStatistics(start, end, top, serverName);
    }

    @Override
    public List<CallStatisticsDetailBo> callSystemDetailStatistics(String start, String end, String[] serverNames) {
        List<CallStatisticsDetailBo> result = new ArrayList<>();
        for (String serverName : serverNames) {
            CallStatisticsDetailBo detailBo = new CallStatisticsDetailBo();
            detailBo.setServerName(serverName);
            List<CallStatisticsBo> bos = callStatisticsExtMapper.selectCallSystemDetail(start, end, serverName);
            bos.forEach(bo -> {
                detailBo.addTotalCount(bo.getPeriod(), bo.getTotalCount());
                detailBo.addSuccessCount(bo.getPeriod(), bo.getSuccessCount());
                detailBo.addFailCount(bo.getPeriod(), bo.getFailCount());
            });
            result.add(detailBo);
        }
        return result;
    }

    @Override
    public List<CallStatisticsDetailBo> callInterfaceDetailStatistics(String start, String end, String[] interfaceNames) {
        List<CallStatisticsDetailBo> result = new ArrayList<>();
        for (String uri : interfaceNames) {
            CallStatisticsDetailBo detailBo = new CallStatisticsDetailBo();
            detailBo.setUri(uri);
            List<CallStatisticsBo> bos = callStatisticsExtMapper.selectCallInterfaceDetail(start, end, uri);
            bos.forEach(bo -> {
                detailBo.setServerName(bo.getServerName());
                detailBo.addTotalCount(bo.getPeriod(), bo.getTotalCount());
                detailBo.addSuccessCount(bo.getPeriod(), bo.getSuccessCount());
                detailBo.addFailCount(bo.getPeriod(), bo.getFailCount());
            });
            result.add(detailBo);
        }
        return result;
    }
}
