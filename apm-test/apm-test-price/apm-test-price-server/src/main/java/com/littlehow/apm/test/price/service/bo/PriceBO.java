package com.littlehow.apm.test.price.service.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 价格响应
 *
 * @author littlehow
 */
@Data
public class PriceBO {

    @ApiModelProperty("订单编号")
    private String itemNo;

    @ApiModelProperty("原始价格")
    private BigDecimal price;

    @ApiModelProperty("活动编号")
    private String promoteNo;

    @ApiModelProperty("活动价格")
    private BigDecimal promotePrice;
}
