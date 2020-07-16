package com.littlehow.apm.test.order.controller;

import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.test.api.PageReq;
import com.littlehow.apm.test.api.PageResult;
import com.littlehow.apm.test.order.api.client.OrderQueryClient;
import com.littlehow.apm.test.order.api.vo.OrderNoVO;
import com.littlehow.apm.test.order.api.vo.OrderVO;
import com.littlehow.apm.test.order.service.OrderQueryService;
import com.littlehow.apm.test.order.service.bo.OrderBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单查询
 * @author littlehow
 */
@RestController
@Slf4j
public class OrderQueryController implements OrderQueryClient {

    @Autowired
    private OrderQueryService orderQueryService;

    @Override
    public OrderVO queryOrderByNo(@RequestBody @Valid OrderNoVO orderNo) {
        return ApmBeanUtils.copyNewOnNull(orderQueryService.queryByOrderNo(orderNo.getOrderNo()), OrderVO.class);
    }

    @Override
    public List<OrderVO> queryOrderByUserNo(@PathVariable String userNo) {
        return orderQueryService.queryByUserNo(userNo).stream()
                .map(o -> ApmBeanUtils.copyNewOnNull(o, OrderVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<OrderVO> queryOrderByPaging(@RequestBody @Valid PageReq pageReq) {
        PageResult<OrderBO> bos = orderQueryService.queryOrder(pageReq.getPageIndex(), pageReq.getPageSize());
        return new PageResult<>(bos.getPageSize(), bos.getPageIndex(), bos.getTotal(),
                bos.getData().stream().map(o -> ApmBeanUtils.copyNewOnNull(o, OrderVO.class))
                        .collect(Collectors.toList()));
    }
}
