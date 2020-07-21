package com.littlehow.apm.test.shop.controller;

import com.littlehow.apm.test.shop.api.client.ShopClient;
import com.littlehow.apm.test.shop.api.vo.ShopInfoVO;
import com.littlehow.apm.test.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商铺接口
 *
 * @author littlehow
 */
@RestController
public class ShopController implements ShopClient {

    @Autowired
    private ShopService shopService;

    @Override
    public ShopInfoVO shopInfo(@PathVariable String shopNo) {
        return shopService.queryShopInfo(shopNo);
    }
}
