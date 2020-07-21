package com.littlehow.apm.test.boss.controller;

import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.test.api.PageReq;
import com.littlehow.apm.test.api.PageResult;
import com.littlehow.apm.test.boss.config.UserContext;
import com.littlehow.apm.test.order.api.client.OrderOperationClient;
import com.littlehow.apm.test.order.api.client.OrderQueryClient;
import com.littlehow.apm.test.order.api.vo.*;
import com.littlehow.apm.test.price.api.client.PriceClient;
import com.littlehow.apm.test.price.api.vo.PriceVO;
import com.littlehow.apm.test.user.api.client.UserClient;
import com.littlehow.apm.test.user.api.vo.UserAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * boss调用下单信息
 * 混合调用
 *
 * @author littlehow
 */
@RestController
@RequestMapping("/apm/test/boss/order")
@Api(tags = "boss下单相关接口")
public class BossOrderController {

    @Autowired
    private OrderQueryClient orderQueryClient;

    @Autowired
    private OrderOperationClient orderOperationClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private PriceClient priceClient;

    @ApiOperation("boss用户创建订单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(@RequestBody @Valid OrderItemVO orderItem) {
        String userNo = UserContext.getUserNo();
        // 查询用户余额
        UserAccountVO accountVO = userClient.getAccount(userNo);
        // 查询价格
        PriceVO priceVO = priceClient.queryByItemNo(orderItem.getItemNo(), userNo);
        // 判断金额是否超过下单额, 优先取活动价格
        BigDecimal price = Optional.ofNullable(priceVO.getPromotePrice()).orElse(priceVO.getPrice());
        ApmAssert.isTrue(accountVO.getAmount().compareTo(price.multiply(new BigDecimal(orderItem.getCount())))  >= 0,
                "用户{0}的余额不足", userNo);
        OrderCreateVO createVO = new OrderCreateVO();
        createVO.setUserNo(UserContext.getUserNo());
        createVO.setOrderItem(orderItem);
        return orderOperationClient.createOrder(createVO);
    }

    @ApiOperation("boss修改订单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateOrder(@RequestBody @Valid OrderUpdateVo orderUpdate) {
        orderOperationClient.updateOrder(orderUpdate);
    }

    @ApiOperation("根据订单编号查询订单信息")
    @RequestMapping(value = "/queryByOrderNo", method = RequestMethod.POST)
    public OrderVO queryOrderByNo(@RequestBody @Valid OrderNoVO orderNo) {
        return orderQueryClient.queryOrderByNo(orderNo);
    }

    @ApiOperation("查询用户订单信息(最新10条)")
    @RequestMapping(value = "/query/ten", method = RequestMethod.POST)
    public List<OrderVO> queryOrderByUserNo() {
        return orderQueryClient.queryOrderByUserNo(UserContext.getUserNo());
    }

    @ApiOperation("分页查询订单信息")
    @RequestMapping(value = "/order/paging", method = RequestMethod.POST)
    public PageResult<OrderVO> queryOrderByPaging(@RequestBody @Valid PageReq pageReq) {
        return orderQueryClient.queryOrderByPaging(pageReq);
    }
}
