package com.littlehow.apm.test.price.service;

import com.littlehow.apm.test.price.service.bo.PriceBO;
import com.littlehow.apm.test.promote.api.client.PromoteClient;
import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteBatchReqVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.littlehow.apm.test.base.util.RandomUtils.*;

@Service
public class PriceService {

    @Autowired
    private PromoteClient promoteClient;

    public PriceBO getItemPrice(String itemNo, String userNo) {
        PriceBO priceBO = new PriceBO();
        priceBO.setItemNo(itemNo);
        priceBO.setPrice(randomPrice());
        // 远程调用活动信息, 这里不捕捉异常，直接上抛
        QueryPromoteReqVO req = new QueryPromoteReqVO();
        req.setItemNo(itemNo);
        req.setUserNo(userNo);
        PromoteVO promoteVO = promoteClient.queryPromote(req);
        if (promoteVO != null) {
            priceBO.setPromoteNo(promoteVO.getPromoteNo());
            priceBO.setPromotePrice(promoteVO.getPromotePrice());
        }
        return priceBO;
    }

    public List<PriceBO> getItemsPrice(List<String> itemNos, String userNo) {
        List<PriceBO> priceBOS = new ArrayList<>(itemNos.size());
        // 远程获取活动信息
        QueryPromoteBatchReqVO batchReq = new QueryPromoteBatchReqVO();
        batchReq.setItemNo(itemNos);
        batchReq.setUserNo(userNo);

        // itemNo : promote映射
        Map<String, PromoteVO> promoteMap = promoteClient.queryPromoteBatch(batchReq)
                .stream()
                // 第三个参数防止重复造成唯一约束异常 使用HashMap储存
                .collect(Collectors.toMap(PromoteVO::getItemNo, o -> o, (o1, o2) -> o2, HashMap::new));

        itemNos.forEach( o -> {
            PriceBO priceBO = new PriceBO();
            priceBO.setPrice(randomPrice());
            priceBO.setItemNo(o);
            PromoteVO promote = promoteMap.get(o);
            if (promote != null) {
                priceBO.setPromoteNo(promote.getPromoteNo());
                priceBO.setPromotePrice(promote.getPromotePrice());
            }
            priceBOS.add(priceBO);
        });
        return priceBOS;
    }
}
