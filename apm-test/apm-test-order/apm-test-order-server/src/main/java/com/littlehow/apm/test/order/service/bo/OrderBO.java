package com.littlehow.apm.test.order.service.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * 订单信息
 *
 * @author littlehow
 */
@Getter
@Setter
@Builder
public class OrderBO {

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
