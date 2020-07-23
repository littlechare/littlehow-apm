package com.littlehow.apm.test.order.api.client;

import com.littlehow.apm.test.api.PageReq;
import com.littlehow.apm.test.api.PageResult;
import com.littlehow.apm.test.order.api.vo.OrderNoVO;
import com.littlehow.apm.test.order.api.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * 示例订单查询接口，将不会有持久化动作
 *
 * @author littlehow
 */
@Api(tags = "订单查询接口")
@FeignClient("apm-order")
@RequestMapping("/apm/test/order")
public interface OrderQueryClient {

    @ApiOperation("根据订单编号查询订单信息")
    @RequestMapping(value = "/queryByOrderNo", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    OrderVO queryOrderByNo(OrderNoVO orderNo);

    @ApiOperation("查询用户订单信息(最新10条)")
    @RequestMapping(value = "/{userNo}/order/ten", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    List<OrderVO> queryOrderByUserNo(@PathVariable("userNo") String userNo);

    @ApiOperation("分页查询订单信息")
    @RequestMapping(value = "/order/paging", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    PageResult<OrderVO> queryOrderByPaging(PageReq pageReq);
}
