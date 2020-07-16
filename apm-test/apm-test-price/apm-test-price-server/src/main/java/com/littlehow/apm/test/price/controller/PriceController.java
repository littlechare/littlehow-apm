package com.littlehow.apm.test.price.controller;

import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.test.price.api.client.PriceClient;
import com.littlehow.apm.test.price.api.vo.BatchItemVO;
import com.littlehow.apm.test.price.api.vo.PriceVO;
import com.littlehow.apm.test.price.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author littlehow
 */
@RestController
public class PriceController implements PriceClient {

    @Autowired
    private PriceService priceService;

    @Override
    public PriceVO queryByItemNo(@RequestParam("itemNo") String itemNo, @RequestParam(required = false) String userNo) {
        return ApmBeanUtils.copyNewOnNull(priceService.getItemPrice(itemNo, userNo), PriceVO.class);
    }

    @Override
    public List<PriceVO> queryPrice(@RequestBody @Valid BatchItemVO batchItem) {
        return priceService.getItemsPrice(batchItem.getItemNos(), batchItem.getUserNo())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, PriceVO.class))
                .collect(Collectors.toList());
    }
}
