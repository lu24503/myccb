/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.price;

import com.shop.cereshop.app.service.price.CerePriceRuleService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.tool.CerePriceProduct;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import com.shop.cereshop.commons.domain.tool.CereShopPrice;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定价捆绑模块
 */
@RestController
@RequestMapping("price")
@Slf4j(topic = "PriceController")
@Api(value = "定价捆绑模块", tags = "定价捆绑模块")
public class PriceController {

    @Autowired
    private CerePriceRuleService cerePriceRuleService;

    /**
     * 定价捆绑规则查询
     * @param param
     * @return
     */
    @GetMapping("selectByPriceId")
    @ApiOperation(value = "定价捆绑规则查询")
    public Result<List<CerePriceRule>> selectByPriceId(CerePriceRule param) throws CoBusinessException,Exception{
        List<CerePriceRule> detailList=cerePriceRuleService.findRules(param.getPriceId());
        return new Result(detailList, CoReturnFormat.SUCCESS);
    }

    /**
     * 根据店铺id查询定价捆绑规则
     * @param param
     * @return
     */
    @GetMapping("selectByShopId")
    @ApiOperation(value = "根据店铺id查询定价捆绑规则")
    public Result<List<CerePriceRule>> selectByShopId(CereShopPrice param) throws CoBusinessException,Exception{
        List<CerePriceRule> detailList=cerePriceRuleService.findRulesByShopId(param.getShopId());
        return new Result(detailList, CoReturnFormat.SUCCESS);
    }
}
