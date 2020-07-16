package com.littlehow.apm.test.order.service;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.test.base.util.IDCreator;
import com.littlehow.apm.test.order.service.bo.OrderItemBO;
import com.littlehow.apm.test.price.api.client.PriceClient;
import com.littlehow.apm.test.price.api.vo.BatchItemVO;
import com.littlehow.apm.test.price.api.vo.PriceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderOperatorService {
    static final String ORDER_PREFIX = "DD";

    @Autowired
    private PriceClient priceClient;

    public String createOrder(String userNo, String itemNo, String shopNo, Integer count) {
        String orderNo =  IDCreator.get(ORDER_PREFIX);
        log.info("创建订单编号:{}", orderNo);
        // 获取价格信息
        PriceVO priceVO = priceClient.queryByItemNo(itemNo, userNo);
        log.info("用户[{}]的商品价格信息:{}", userNo, JSONObject.toJSONString(priceVO));
        return orderNo;
    }

    public String createBatchOrder(String userNo, List<OrderItemBO> orderItems) {
        String orderNo =  IDCreator.get(ORDER_PREFIX);
        log.info("创建订单编号:{}", orderNo);
        // 获取价格信息
        BatchItemVO batchItemVO = new BatchItemVO();
        batchItemVO.setUserNo(userNo);
        batchItemVO.setItemNos(orderItems.stream().map(OrderItemBO::getItemNo).collect(Collectors.toList()));
        List<PriceVO> prices = priceClient.queryPrice(batchItemVO);
        log.info("用户[{}]的商品价格信息:{}", userNo, JSONObject.toJSONString(prices));
        return orderNo;
    }

    public void updateOrder(String orderNo, String status) {
        // do nothing
    }
}
