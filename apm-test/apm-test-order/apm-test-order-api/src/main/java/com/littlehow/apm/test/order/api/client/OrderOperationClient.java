package com.littlehow.apm.test.order.api.client;

import com.littlehow.apm.test.order.api.vo.BatchOrderCreateVO;
import com.littlehow.apm.test.order.api.vo.OrderCreateVO;
import com.littlehow.apm.test.order.api.vo.OrderUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 示例订单操作接口，将不会有持久化动作
 *
 * @author littlehow
 */
@Api(tags = "订单操作接口")
@FeignClient("apm-order")
@RequestMapping("/apm/test/order")
public interface OrderOperationClient {

    @ApiOperation("下单操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    String createOrder(OrderCreateVO orderCreate);

    @ApiOperation("批量下单操作")
    @RequestMapping(value = "/create/batch", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    String createBatchOrder(BatchOrderCreateVO batchOrderCreate);

    @ApiOperation("修改订单")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    void updateOrder(OrderUpdateVo orderUpdate);
}
