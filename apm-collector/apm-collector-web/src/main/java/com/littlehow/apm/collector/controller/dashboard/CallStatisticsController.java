package com.littlehow.apm.collector.controller.dashboard;


import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.api.req.CallStatisticsReq;
import com.littlehow.apm.collector.api.resp.CallStatisticsDetailResp;
import com.littlehow.apm.collector.api.resp.CallStatisticsResp;
import com.littlehow.apm.collector.biz.IStatisticsService;
import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import com.littlehow.apm.collector.orm.model.CallStatisticsDetailBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "调用统计相关接口")
@RestController
@RequestMapping("/apm/collection/dashboard/statistics")
@Slf4j
public class CallStatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    @ApiOperation("调用量统计")
    @RequestMapping(value = "queryCall", method = RequestMethod.POST)
    public List<CallStatisticsResp> queryCallStatistics(@RequestBody @Valid CallStatisticsReq req) {
        validTimeRange(req, 7 * 24);
        List<CallStatisticsBo> callStatisticsBos = statisticsService.callStatistics(req.getType(),
                req.getStartTime(), req.getEndTime(), req.getTop(), req.getServerName());
        if (CollectionUtils.isEmpty(callStatisticsBos)) {
            return new ArrayList<>();
        }
        return callStatisticsBos.stream().map(o -> ApmBeanUtils.copyNewOnNull(o, CallStatisticsResp.class))
                .collect(Collectors.toList());
    }

    @ApiOperation("调用量分时统计")
    @RequestMapping(value = "queryDetail", method = RequestMethod.POST)
    public List<CallStatisticsDetailResp> queryDetailStatistics(@RequestBody @Valid CallStatisticsReq req) {
        validTimeRange(req, 12);
        ApmAssert.notNull(req.getServerName(), "服务名称或接口名称不可为空");
        String[] serverNames = req.getServerName().split(",");
        //按每个系统查询或接口查询
        List<CallStatisticsDetailBo> detailBos = statisticsService.callDetailStatistics(req.getType(),
                req.getStartTime(), req.getEndTime(), serverNames);
        return detailBos.stream().map(o -> ApmBeanUtils.copyNewOnNull(o, CallStatisticsDetailResp.class))
                .collect(Collectors.toList());
    }

    /**
     * 时间范围控制在7天左右, 防止数据统计太大
     * @param req -
     */
    private void validTimeRange(CallStatisticsReq req, int hours) {
        Duration duration = Duration.between(req.getStartTime(), req.getEndTime());
        ApmAssert.isTrue(duration.toHours() <= hours, "时间范围不能大于" + hours + "小时");
    }
}
