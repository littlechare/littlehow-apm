package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInfo;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ServerInfoMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class ServerInfoServiceImpl extends ServiceImpl<ServerInfoMapper, ServerInfo> implements IServerInfoService {

}
