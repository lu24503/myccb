/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.discount;

import com.shop.cereshop.app.page.discount.DiscountIndex;
import com.shop.cereshop.app.page.discount.PlatformDiscountProduct;
import com.shop.cereshop.app.param.discount.DiscountParam;
import com.shop.cereshop.app.param.discount.PlatformDiscountParam;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformDiscount;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 平台端限时折扣专区
 */
@RestController
@RequestMapping("platform-discount")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "DiscountController")
@Api(value = "平台端限时折扣专区", tags = "平台端限时折扣专区")
public class PlatformDiscountController {

    @Autowired
    private CerePlatformDiscountService cerePlatformDiscountService;

    /**
     * 限时折扣专区首页数据查询
     * @return
     */
    @GetMapping("queryPlatformDiscount")
    @ApiOperation(value = "限时折扣专区首页数据查询")
    public Result<CerePlatformDiscount> queryPlatformDiscount(CerePlatformDiscount param) throws CoBusinessException {
        try {
            CerePlatformDiscount discount = cerePlatformDiscountService.queryPlatformDiscount(param.getDiscountId());
            return new Result(discount, CoReturnFormat.SUCCESS);
        } catch (Exception e) {
            log.error("queryPlatformDiscount fail: discountId = {}", param != null ? param.getDiscountId() : null, e);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }

    /**
     * 限时折扣专区首页数据查询
     * @return
     */
    @PostMapping("queryPlatformDiscountProductList")
    @ApiOperation(value = "限时折扣专区首页商品数据查询")
    public Result<Page<PlatformDiscountProduct>> queryPlatformDiscountProductList(@RequestBody PlatformDiscountParam param) throws CoBusinessException {
        try {
            Page<PlatformDiscountProduct> page = cerePlatformDiscountService.queryPlatformDiscountProductList(param);
            return new Result(page, CoReturnFormat.SUCCESS);
        } catch (Exception e) {
            log.error("queryPlatformDiscount fail: discountId = {}", param != null ? param.getDiscountId() : null, e);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }
}
