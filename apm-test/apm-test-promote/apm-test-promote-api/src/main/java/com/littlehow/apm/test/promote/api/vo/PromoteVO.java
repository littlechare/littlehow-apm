package com.littlehow.apm.test.promote.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author littlehow
 */
@Data
@ApiModel(description = "活动价格信息")
public class PromoteVO {

    @ApiModelProperty("商品编号")
    private String itemNo;

    @ApiModelProperty("活动编号")
    private String promoteNo;

    @ApiModelProperty("活动价格")
    private BigDecimal promotePrice;
}
