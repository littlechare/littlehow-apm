package com.littlehow.apm.test.shop.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 *
 * @author littlehow
 */
@ApiModel(description = "商铺信息")
@Data
public class ShopInfoVO {

    @ApiModelProperty("商铺编号")
    private String shopNo;

    @ApiModelProperty("账户编号")
    private String account;

    @ApiModelProperty("手机号码")
    private String mobilePhone;

    @ApiModelProperty("商铺名称")
    private String shopName;

    @ApiModelProperty("开张时间")
    private LocalDateTime openTime;

    @ApiModelProperty("商品数量")
    private Integer itemCount;

    @ApiModelProperty("活动数量")
    private Integer promoteCount;
}
