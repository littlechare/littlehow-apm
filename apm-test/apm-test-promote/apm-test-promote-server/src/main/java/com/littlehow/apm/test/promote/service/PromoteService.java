package com.littlehow.apm.test.promote.service;

import com.littlehow.apm.test.base.util.IDCreator;
import com.littlehow.apm.test.promote.service.bo.PromoteBO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.littlehow.apm.test.base.util.RandomUtils.*;

/**
 * @author littlehow
 */
@Service
public class PromoteService {

    /**
     * 查询活动信息
     * @param userNo - 用户编号
     * @param itemNo -
     * @return
     */
    public PromoteBO getPromote(String userNo, String itemNo) {
        // 模拟没有活动信息
        if (!hasPromote()) {
            return null;
        }
        return createPromote(itemNo);
    }

    public List<PromoteBO> getPromotes(String userNo, List<String> itemNos) {
        // 不初始化长度，因为长度可能小于itemNos.size()
        List<PromoteBO> promoteBOS = new ArrayList<>();
        itemNos.forEach(o -> {
            if (hasPromote()) {
                promoteBOS.add(createPromote(o));
            }
        });
        return promoteBOS;
    }

    public Integer getShopPromoteCount(String shopNo) {
        return randomCount(40, 150);
    }

    private PromoteBO createPromote(String itemNo) {
        PromoteBO promoteBO = new PromoteBO();
        promoteBO.setItemNo(itemNo);
        promoteBO.setPromoteNo(IDCreator.get());
        promoteBO.setPromotePrice(randomPrice());
        return promoteBO;
    }

    private boolean hasPromote() {
        return randomCount(1, 60) > 35;
    }
}
