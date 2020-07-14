package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * 订单信息
 *
 * @author littlehow
 */
@Data
@ApiModel(description = "订单信息")
public class OrderVO {

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("商品编号")
    private String itemNo;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("订单状态")
    private String status;

    @ApiModelProperty("单价(可能是活动价)")
    private BigDecimal price;

    @ApiModelProperty("总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("订单创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("订单修改时间")
    private LocalDateTime updateTime;
}
