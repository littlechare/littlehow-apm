package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仅仅是示例(所以不演示多商品同时下单)
 * 也不严谨推敲用户编号为什么在请求体
 *
 * @author littlehow
 */
@Data
@ApiModel(description = "下单请求")
public class OrderCreateVO {

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("商品编号")
    private String itemNo;

    @ApiModelProperty("商家编号")
    private String shopNo;

    @ApiModelProperty("数量")
    private Integer count;

}
