package com.littlehow.apm.test.order.service;

import com.littlehow.apm.test.api.PageResult;
import com.littlehow.apm.test.base.util.IDCreator;
import com.littlehow.apm.test.order.service.bo.OrderBO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.littlehow.apm.test.base.util.RandomUtils.*;

@Service
public class OrderQueryService {

    /**
     * 查询用户订单，根据订单号
     * @param orderNo -
     * @return
     */
    public OrderBO queryByOrderNo(String orderNo) {
        return createOrderBO(orderNo, IDCreator.get());
    }

    /**
     * 查询用户前10条订单
     * @param userNo -
     * @return
     */
    public List<OrderBO> queryByUserNo(String userNo) {
        // 最多生成10条， 最少生成3条
        int count = randomCount(3, 10);
        List<OrderBO> orders = new ArrayList<>(count);
        while (count -- > 0) {
            orders.add(createOrderBO(IDCreator.get(OrderOperatorService.ORDER_PREFIX), userNo));
        }
        return orders;
    }

    /**
     * 查询订单信息
     * @param pageIndex -
     * @param pageSize  -
     * @return -
     */
    public PageResult<OrderBO> queryOrder(int pageIndex, int pageSize) {
        int count = randomCount(2, pageSize);
        List<OrderBO> orders = new ArrayList<>(pageSize);
        while (count -- > 0) {
            orders.add(createOrderBO(IDCreator.get(OrderOperatorService.ORDER_PREFIX), IDCreator.get()));
        }

        int totalSize = (pageIndex - 1) * pageSize + orders.size();
        if (orders.size() == pageSize) {
            totalSize = randomCount(totalSize, 3 * totalSize);
        }
        return new PageResult<>(pageSize, pageIndex, (long) totalSize, orders);
    }

    private OrderBO createOrderBO(String orderNo, String userNo) {
        int count = randomCount();
        BigDecimal price = randomPrice();
        return OrderBO.builder()
                .count(randomCount())
                .createTime(randomDateTime())
                .itemNo(IDCreator.get())
                .orderNo(orderNo)
                .userNo(userNo)
                .price(price)
                .status(randomStatus())
                .updateTime(randomDateTime())
                .totalPrice(new BigDecimal(count).multiply(price))
                .build();
    }
}
