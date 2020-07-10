package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInterface;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ServerInterfaceMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerInterfaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class ServerInterfaceServiceImpl extends ServiceImpl<ServerInterfaceMapper, ServerInterface> implements IServerInterfaceService {

}
