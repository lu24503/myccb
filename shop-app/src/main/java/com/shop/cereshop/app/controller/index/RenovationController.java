/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.index;

import com.shop.cereshop.app.page.canvas.CanvasProduct;
import com.shop.cereshop.app.page.discount.ShopDiscountDetail;
import com.shop.cereshop.app.page.groupwork.ShopGroupWorkUDetail;
import com.shop.cereshop.app.page.seckill.ShopSeckillDetail;
import com.shop.cereshop.app.param.canvas.CanvasProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkService;
import com.shop.cereshop.app.service.price.CerePriceProductService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.common.Page;
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
 * 店铺装修
 */
@RestController
@RequestMapping("renovation")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "RenovationController")
@Api(value = "店铺装修", tags = "店铺装修")
public class RenovationController {

    @Autowired
    private CereShopGroupWorkService cereShopGroupWorkService;

    @Autowired
    private CereShopSeckillService cereShopSeckillService;

    @Autowired
    private CereShopDiscountService cereShopDiscountService;

    @Autowired
    private CerePriceProductService cerePriceProductService;

    /**
     * 选择拼团活动查询
     * @return
     */
    @GetMapping("getGroupWorks")
    @ApiOperation(value = "选择拼团活动查询")
    public Result<List<ShopGroupWorkUDetail>> getGroupWorks(RenovationParam param) throws CoBusinessException{
        List<ShopGroupWorkUDetail> list=cereShopGroupWorkService.getGroupWorks(param);
        return new Result(list, CoReturnFormat.SUCCESS);
    }

    /**
     * 选择秒杀活动查询
     * @return
     */
    @GetMapping("getSeckills")
    @ApiOperation(value = "选择秒杀活动查询")
    public Result<List<ShopSeckillDetail>> getSeckills(RenovationParam param) throws CoBusinessException{
        List<ShopSeckillDetail> list=cereShopSeckillService.getSeckills(param);
        return new Result(list, CoReturnFormat.SUCCESS);
    }

    /**
     * 选择限时折扣活动查询
     * @return
     */
    @GetMapping("getDiscounts")
    @ApiOperation(value = "选择限时折扣活动查询")
    public Result<List<ShopDiscountDetail>> getDiscounts(RenovationParam param) throws CoBusinessException{
        List<ShopDiscountDetail> list=cereShopDiscountService.getDiscounts(param);
        return new Result(list, CoReturnFormat.SUCCESS);
    }

    /**
     * 画布选择定价捆绑商品数据
     * @param param
     * @return
     */
    @GetMapping("getPriceProducts")
    @ApiOperation(value = "画布选择定价捆绑商品数据")
    public Result<Page<CanvasProduct>> getPriceProducts(CanvasProductParam param) throws CoBusinessException{
        Page page=cerePriceProductService.getPriceProducts(param);
        return new Result(page);
    }
}

