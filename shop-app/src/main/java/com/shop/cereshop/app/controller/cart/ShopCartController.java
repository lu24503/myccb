/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.cart;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.cart.ShopCart;
import com.shop.cereshop.app.param.cart.*;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 购物车模块
 */
@RestController
@RequestMapping("cart")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "ShopCartController")
@Api(value = "购物车模块", tags = "购物车模块")
public class ShopCartController {

    @Autowired
    private CereShopCartService cereShopCartService;

    /**
     * 加入购物车
     * @param param
     * @return
     */
    @PostMapping("addCart")
    @NoRepeatSubmit
    @ApiOperation(value = "加入购物车")
    @NoRepeatWebLog
    public Result addCart(@RequestBody AddCartParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.addCart(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"加入购物车", GsonUtil.objectToGson(param));
    }

    /**
     * 更换规格
     * @param param
     * @return
     */
    @RequestMapping(value = "updateSku", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "更换规格")
    @NoRepeatWebLog
    public Result updateSku(@RequestBody UpdateSkuParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.updateSku(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"更换规格", GsonUtil.objectToGson(param));
    }

    /**
     * 批量加入购物车
     * @param param
     * @return
     */
    @PostMapping("addBatchCart")
    @NoRepeatSubmit
    @ApiOperation(value = "批量加入购物车")
    @NoRepeatWebLog
    public Result addBatchCart(@RequestBody BatchCartParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.addBatchCart(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"批量加入购物车", GsonUtil.objectToGson(param));
    }

    /**
     * 再次购买
     * @return
     */
    @RequestMapping(value = "buyAgain", method = {RequestMethod.PUT, RequestMethod.POST})
    @ApiOperation(value = "再次购买")
    @NoRepeatWebLog
    public Result buyAgain(@RequestBody BuyerAgainParam param,HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.buyAgain(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"再次购买", GsonUtil.objectToGson(param));
    }

    /**
     * 修改购物车商品数量
     * @param param
     * @return
     */
    @RequestMapping(value = "updateNumber", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改购物车商品数量")
    @NoRepeatWebLog
    public Result updateNumber(@RequestBody CartUpdateParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.updateNumber(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改购物车商品数量", GsonUtil.objectToGson(param));
    }

    /**
     * 删除购物车商品
     * @param param
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除购物车商品")
    @NoRepeatWebLog
    public Result delete(@RequestBody DeleteParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.delete(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除购物车商品", GsonUtil.objectToGson(param));
    }

    /**
     * 清空购物车
     * @return
     */
    @RequestMapping(value = "deleteAll", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "清空购物车")
    @NoRepeatWebLog
    public Result deleteAll(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.deleteAll(user.getBuyerUserId());
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"清空购物车","");
    }

    /**
     * 选中商品
     * @return
     */
    @RequestMapping(value = "selected", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "选中商品")
    @NoRepeatWebLog
    public Result selected(@RequestBody SelectedParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCartService.selected(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"选中商品", GsonUtil.objectToGson(param));
    }

    /**
     * 购物车查询
     * @return
     */
    @GetMapping("getCart")
    @ApiOperation(value = "购物车查询")
    public Result<List<ShopCart>> getCart(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<ShopCart> list=cereShopCartService.getCart(user);
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 清空失效购物车商品
     * @return
     */
    @GetMapping("clearInvalidSku")
    @ApiOperation(value = "清空失效购物车商品")
    public Result<Boolean> clearInvalidSku(HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Boolean result=cereShopCartService.clearInvalidSku(user.getBuyerUserId());
        return new Result(result,CoReturnFormat.SUCCESS);
    }
}
