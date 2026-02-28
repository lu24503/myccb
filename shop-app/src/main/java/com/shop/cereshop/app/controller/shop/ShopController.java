/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.shop;

import com.shop.cereshop.app.page.banner.ShopBanner;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.page.shop.Shop;
import com.shop.cereshop.app.page.shop.ShopClassify;
import com.shop.cereshop.app.page.shop.ShopIndex;
import com.shop.cereshop.app.param.shop.ShopParam;
import com.shop.cereshop.app.param.shop.ShopPosterParam;
import com.shop.cereshop.app.service.business.CereBusinessBuyerUserService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.business.CereBusinessBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 店铺模块
 */
@RestController
@RequestMapping("shop")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "ShopController")
@Api(value = "店铺模块", tags = "店铺模块")
public class ShopController {

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CereBusinessBuyerUserService cereBusinessBuyerUserService;

    /**
     * 店铺商品查询
     * @return
     */
    @GetMapping("getShopProducts")
    @ApiOperation(value = "商品搜索查询")
    public Result<Shop> getShopProducts(ShopParam param, HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Shop shop= cerePlatformShopservice.getShopProducts(param,user);
        return new Result(shop, CoReturnFormat.SUCCESS);
    }

    /**
     * 店铺搜索
     * @return
     */
    @GetMapping("getShops")
    @ApiOperation(value = "店铺搜索")
    public Result<Page<SettlementShop>> getShops(ShopParam param, HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page= cerePlatformShopservice.getShops(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询店铺分类
     * @return
     */
    @GetMapping("getShopClassify")
    @ApiOperation(value = "查询店铺分类")
    public Result<List<ShopClassify>> getShopClassify(ShopParam param) throws CoBusinessException{
        List<ShopClassify> list= cerePlatformShopservice.getShopClassify(param);
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询店铺banner
     * @return
     */
    @GetMapping("getShopBanner")
    @ApiOperation(value = "查询店铺banner")
    public Result<List<ShopBanner>> getShopBanner(ShopParam param) throws CoBusinessException{
        List<ShopBanner> list= cerePlatformShopservice.getShopBanner(param.getShopId());
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 店铺首页查询
     * @return
     */
    @GetMapping("getIndex")
    @ApiOperation(value = "店铺首页查询")
    public Result<ShopIndex> getIndex(ShopParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        ShopIndex index= cerePlatformShopservice.getIndex(param,user);
        return new Result(index,CoReturnFormat.SUCCESS);
    }

    /**
     * 获取分享图片
     * @param param
     * @return
     */
    @GetMapping("getSharePic")
    @ApiOperation(value = "获取分享图片")
    public Result<String> getSharePic(ShopPosterParam param, HttpServletRequest request) throws CoBusinessException {
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        String sharePicUrl = cerePlatformShopservice.getSharePic(param,user);
        Result<String> result = new Result();
        result.setCode(CoReturnFormat.SUCCESS);
        result.setData(sharePicUrl);
        return result;
    }

    @PostMapping("addBusinessBuyerUser")
    @ApiOperation(value = "添加客户访问商家的记录")
    public Result<Boolean> addBusinessBuyerUser(@RequestBody CereBusinessBuyerUser cereBusinessBuyerUser, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
            if (user != null && user.getBuyerUserId() != null) {
                cereBusinessBuyerUser.setBuyerUserId(user.getBuyerUserId());
                cereBusinessBuyerUserService.addBusinessBuyerUser(cereBusinessBuyerUser);
            }
        }
        return new Result(true, CoReturnFormat.SUCCESS);
    }
}
