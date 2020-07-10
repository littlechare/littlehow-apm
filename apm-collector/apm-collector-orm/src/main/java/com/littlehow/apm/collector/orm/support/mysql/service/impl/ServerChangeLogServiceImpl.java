package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerChangeLog;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ServerChangeLogMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务状态变化日志表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class ServerChangeLogServiceImpl extends ServiceImpl<ServerChangeLogMapper, ServerChangeLog> implements IServerChangeLogService {

}
