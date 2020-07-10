package com.littlehow.apm.collector.orm.support.mysql.service.impl;

import com.littlehow.apm.collector.orm.support.mysql.dao.po.InterfaceDependence;
import com.littlehow.apm.collector.orm.support.mysql.dao.mapper.InterfaceDependenceMapper;
import com.littlehow.apm.collector.orm.support.mysql.service.IInterfaceDependenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口依赖关系 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Service
public class InterfaceDependenceServiceImpl extends ServiceImpl<InterfaceDependenceMapper, InterfaceDependence> implements IInterfaceDependenceService {

}
