package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.CallStatistics;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.CallStatisticsMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.ICallStatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调用统计 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class CallStatisticsServiceImpl extends ServiceImpl<CallStatisticsMapper, CallStatistics> implements ICallStatisticsService {

}
