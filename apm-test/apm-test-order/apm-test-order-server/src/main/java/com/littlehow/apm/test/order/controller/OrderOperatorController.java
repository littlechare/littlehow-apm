package com.littlehow.apm.test.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.test.order.api.client.OrderOperationClient;
import com.littlehow.apm.test.order.api.vo.OrderCreateVO;
import com.littlehow.apm.test.order.api.vo.OrderUpdateVo;
import com.littlehow.apm.test.order.service.OrderOperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class OrderOperatorController implements OrderOperationClient {

    @Autowired
    private OrderOperatorService orderOperatorService;

    @Override
    public String createOrder(@RequestBody @Valid OrderCreateVO orderCreate) {
        log.info("创建订单:{}", JSONObject.toJSONString(orderCreate));
        return orderOperatorService.createOrder(orderCreate.getUserNo(), orderCreate.getItemNo(),
                orderCreate.getShopNo(), orderCreate.getCount());
    }

    @Override
    public void updateOrder(OrderUpdateVo orderUpdate) {
        log.info("修改订单:{}", JSONObject.toJSONString(orderUpdate));
        orderOperatorService.updateOrder(orderUpdate.getOrderNo(), orderUpdate.getStatus(), orderUpdate.getCount());
    }
}
