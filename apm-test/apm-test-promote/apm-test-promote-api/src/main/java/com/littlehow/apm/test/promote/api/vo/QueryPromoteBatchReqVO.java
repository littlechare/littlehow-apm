package com.littlehow.apm.test.promote.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author littlehow
 */
@Data
public class QueryPromoteBatchReqVO {
    @ApiModelProperty("用户编号(可为空)")
    private String userNo;

    @ApiModelProperty("商品编号")
    @NotEmpty(message = "商品编号不可为空")
    private List<String> itemNo;
}
