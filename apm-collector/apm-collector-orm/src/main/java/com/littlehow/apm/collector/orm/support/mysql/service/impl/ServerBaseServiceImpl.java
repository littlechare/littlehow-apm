package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerBase;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ServerBaseMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务基础信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class ServerBaseServiceImpl extends ServiceImpl<ServerBaseMapper, ServerBase> implements IServerBaseService {

}
