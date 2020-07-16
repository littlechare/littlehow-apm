package com.littlehow.apm.test.price.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 批量获取商品价格
 * 模拟千人千价，所以如果有用户编号，可以先去活动查看价格
 *
 * @author littlehow
 */
@Setter
@Getter
public class BatchItemVO {
    @ApiModelProperty("用户编号(可不传)")
    private String userNo;

    @ApiModelProperty("商品编号集合")
    @NotEmpty(message = "商品编号不可为空")
    private List<String> itemNos;
}
