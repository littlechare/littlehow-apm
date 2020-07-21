package com.littlehow.apm.test.boss.controller;

/**
 * @author littlehow
 */

import com.littlehow.apm.test.promote.api.client.PromoteClient;
import com.littlehow.apm.test.promote.api.client.PromoteUrlClient;
import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteBatchReqVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * boss调用活动信息
 *
 * @author littlehow
 */
@RestController
@RequestMapping("/apm/test/boss/promote")
@Api(tags = "boss活动相关接口")
public class BossPromoteController {
    @Autowired
    private PromoteClient promoteClient;

    @Autowired
    private PromoteUrlClient promoteUrlClient;

    @ApiOperation("添加活动")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public PromoteVO addPromote(@RequestBody @Valid QueryPromoteReqVO req) {
        return promoteUrlClient.addPromote(req);
    }

    @ApiOperation("查询活动信息")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public PromoteVO queryPromote(@RequestBody @Valid QueryPromoteReqVO req) {
        return promoteClient.queryPromote(req);
    }

    @ApiOperation("查询活动信息")
    @RequestMapping(value = "/query/batch", method = RequestMethod.POST)
    public List<PromoteVO> queryPromoteBatch(@RequestBody @Valid QueryPromoteBatchReqVO req) {
        return promoteClient.queryPromoteBatch(req);
    }

    @ApiOperation("查询活动数量信息")
    @RequestMapping(value = "/shop/count", method = RequestMethod.GET)
    public Integer queryShopPromoteCount(@RequestParam("shopNo") String shopNo) {
        return promoteClient.queryShopPromoteCount(shopNo);
    }
}
