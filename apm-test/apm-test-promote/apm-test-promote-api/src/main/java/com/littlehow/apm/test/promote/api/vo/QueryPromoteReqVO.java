package com.littlehow.apm.test.promote.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author littlehow
 */
@Data
@ApiModel(description = "单个商品活动信息请求体")
public class QueryPromoteReqVO {

    @ApiModelProperty("用户编号(可为空)")
    private String userNo;

    @ApiModelProperty("商品编号")
    @NotBlank(message = "商品编号不可为空")
    private String itemNo;
}
