package com.littlehow.apm.test.order.service;

import com.littlehow.apm.test.base.util.IDCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderOperatorService {
    private static final String ORDER_PREFIX = "DD";

    public String createOrder(String userNo, String itemNo, String shopNo, Integer count) {
        String orderNo =  IDCreator.get(ORDER_PREFIX);
        log.info("创建订单编号:{}", orderNo);
        return null;
    }

    public void updateOrder(String orderNo, String status, Integer count) {
        // do nothing
    }
}
