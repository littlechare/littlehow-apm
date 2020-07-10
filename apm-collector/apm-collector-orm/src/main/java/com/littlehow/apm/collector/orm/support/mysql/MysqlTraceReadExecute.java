package com.littlehow.apm.collector.orm.support.mysql;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlehow.apm.base.req.TraceInfoReq;
import com.littlehow.apm.base.resp.PageResult;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.TraceLog;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component("mysqlTraceExecute")
public class MysqlTraceReadExecute extends MysqlTraceExecute {

    @Override
    public PageResult<TraceInfoReq> queryTraceInfo(String traceId, Long startTime, Long endTime, int pageIndex, int pageSize) {
        Page<TraceLog> page = new Page<>(pageIndex, pageSize);
        page.addOrder(OrderItem.desc("id"));
        if (StringUtils.hasText(traceId)
                || hasTime(startTime)
                || hasTime(endTime)) {
            traceLogService.lambdaQuery()
                    .eq(StringUtils.hasText(traceId), TraceLog::getTraceId, traceId)
                    .ge(hasTime(startTime), TraceLog::getStartTime, startTime)
                    .le(hasTime(endTime), TraceLog::getStartTime, endTime)
                    .page(page);
        } else {
            traceLogService.page(page);
        }
        List<TraceLog> records = page.getRecords();
        PageResult<TraceInfoReq> result;
        if (!CollectionUtils.isEmpty(records)) {
            result = new PageResult<>(pageIndex, pageSize, page.getTotal(),
                    records.stream()
                            .map(o -> ApmBeanUtils.copyNewOnNull(o, TraceInfoReq.class))
                            .collect(Collectors.toList()));
        } else {
            result = new PageResult<>(pageIndex, pageIndex, page.getTotal(), new ArrayList<>());
        }
        return result;
    }

    private boolean hasTime(Long time) {
        return time != null && time > 0L;
    }
}
