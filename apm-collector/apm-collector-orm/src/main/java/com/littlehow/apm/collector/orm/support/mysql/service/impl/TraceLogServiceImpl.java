package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.TraceLog;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.TraceLogMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.ITraceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调用日志表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class TraceLogServiceImpl extends ServiceImpl<TraceLogMapper, TraceLog> implements ITraceLogService {

}
