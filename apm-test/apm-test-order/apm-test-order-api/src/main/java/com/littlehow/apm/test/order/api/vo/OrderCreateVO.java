package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 仅仅是示例(所以不演示多商品同时下单)
 * 也不严谨推敲用户编号为什么在请求体
 *
 * @author littlehow
 */
@Data
@ApiModel(description = "下单请求")
public class OrderCreateVO {

    @NotNull(message = "用户编号不可为空")
    @ApiModelProperty("用户编号")
    private String userNo;

    @Valid
    @NotNull(message = "订单商品信息不可为空")
    @ApiModelProperty("订单商品信息")
    private OrderItemVO orderItem;
}
