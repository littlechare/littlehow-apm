package com.littlehow.apm.test.shop.api.client;

import com.littlehow.apm.test.shop.api.vo.ShopInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 商铺接口
 * @author littlehow
 */
@Api(tags = "商铺接口")
@FeignClient("apm-shop")
@RequestMapping("/apm/test/shop")
public interface ShopClient {

    @ApiOperation("查询商铺信息")
    @RequestMapping(value = "/{shopNo}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ShopInfoVO shopInfo(@PathVariable("shopNo") String shopNo);
}
