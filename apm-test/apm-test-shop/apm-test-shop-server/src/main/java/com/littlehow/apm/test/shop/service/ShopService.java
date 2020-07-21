package com.littlehow.apm.test.shop.service;

import com.littlehow.apm.test.base.util.IDCreator;
import com.littlehow.apm.test.promote.api.client.PromoteClient;
import com.littlehow.apm.test.shop.api.vo.ShopInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.littlehow.apm.test.base.util.RandomUtils.*;
/**
 * @author littlehow
 */
@Service
public class ShopService {

    private static String[] shopNames = {
            "littlehow精品店", "传世表行", "小小手机店",
            "chare", "color wolf", "皮卡丘专卖店",
            "熊猫专卖店"
    };

    @Autowired
    private PromoteClient promoteClient;

    /**
     * 这里就偷懒不适用BO了，测试项目耦合没关系
     * @param shopNo -
     * @return -
     */
    public ShopInfoVO queryShopInfo(String shopNo) {
        ShopInfoVO shopInfoVO = new ShopInfoVO();
        shopInfoVO.setAccount(IDCreator.get());
        shopInfoVO.setItemCount(randomCount());
        shopInfoVO.setShopName(shopNames[randomCount(0, shopNames.length - 1)]);
        shopInfoVO.setPromoteCount(promoteClient.queryShopPromoteCount(shopNo));
        shopInfoVO.setOpenTime(randomDateTime());
        shopInfoVO.setShopNo(shopNo);
        shopInfoVO.setMobilePhone(randomMobilePhone());
        return shopInfoVO;
    }
}
