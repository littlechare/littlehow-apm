package com.littlehow.apm.test.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.test.order.api.client.OrderOperationClient;
import com.littlehow.apm.test.order.api.vo.BatchOrderCreateVO;
import com.littlehow.apm.test.order.api.vo.OrderCreateVO;
import com.littlehow.apm.test.order.api.vo.OrderUpdateVo;
import com.littlehow.apm.test.order.service.OrderOperatorService;
import com.littlehow.apm.test.order.service.bo.OrderItemBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class OrderOperatorController implements OrderOperationClient {

    @Autowired
    private OrderOperatorService orderOperatorService;

    @Override
    public String createOrder(@RequestBody @Valid OrderCreateVO orderCreate) {
        log.info("创建订单:{}", JSONObject.toJSONString(orderCreate));
        return orderOperatorService.createOrder(orderCreate.getUserNo(), orderCreate.getOrderItem().getItemNo(),
                orderCreate.getOrderItem().getShopNo(), orderCreate.getOrderItem().getCount());
    }

    @Override
    public String createBatchOrder(@RequestBody @Valid BatchOrderCreateVO batchOrderCreate) {
        log.info("创建订单-多商品:{}", JSONObject.toJSONString(batchOrderCreate));
        return orderOperatorService.createBatchOrder(batchOrderCreate.getUserNo(),
                batchOrderCreate.getOrderItems().stream()
                        .map(o -> ApmBeanUtils.copyNewOnNull(o, OrderItemBO.class))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void updateOrder(@RequestBody @Valid OrderUpdateVo orderUpdate) {
        log.info("修改订单:{}", JSONObject.toJSONString(orderUpdate));
        orderOperatorService.updateOrder(orderUpdate.getOrderNo(), orderUpdate.getStatus());
    }
}
