package com.littlehow.apm.test.boss.controller;

import com.littlehow.apm.test.shop.api.client.ShopClient;
import com.littlehow.apm.test.shop.api.vo.ShopInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * boss调用商铺信息
 * 偷懒使用api提供的VO
 *
 * @author littlehow
 */
@RestController
@RequestMapping("/apm/test/boss/shop")
@Api(tags = "boss商铺相关接口")
public class BossShopController {

    @Autowired
    private ShopClient shopClient;

    @ApiOperation("查询商铺信息")
    @RequestMapping(value = "/{shopNo}", method = RequestMethod.POST)
    public ShopInfoVO shopInfo(@PathVariable String shopNo) {
        return shopClient.shopInfo(shopNo);
    }
}
