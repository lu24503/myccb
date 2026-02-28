/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.shop;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.shop.PlatformShop;
import com.shop.cereshop.app.param.shop.*;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.business.CerePlatformBusinessService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.shop.*;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.business.CerePlatformBusiness;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 入驻
 */
@RestController
@RequestMapping("check")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "ShopCheckController")
@Api(value = "入驻模块", tags = "入驻模块")
public class ShopCheckController {

    @Autowired
    private CerePlatformShopservice cerePlatformShopService;

    @Autowired
    private CereShopPersonalService cereShopPersonalService;

    @Autowired
    private CereShopIndividualBusinessesService cereShopIndividualBusinessesService;

    @Autowired
    private CereShopOtherOrganizationsService cereShopOtherOrganizationsService;

    @Autowired
    private CereShopEnterpriseService cereShopEnterpriseService;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CerePlatformBusinessService cerePlatformBusinessService;

    /**
     * 店铺信息查询
     * @param cerePlatformShop
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "店铺信息查询")
    public Result<PlatformShop> getById(ShopParam cerePlatformShop) throws CoBusinessException{
        PlatformShop shop=cerePlatformShopService.getById(cerePlatformShop.getShopId());
        return new Result(shop,CoReturnFormat.SUCCESS);
    }

    /**
     * 入驻申请（个人）
     * @param param
     * @return
     */
    @PostMapping("personalCheck")
    @NoRepeatSubmit
    @ApiOperation(value = "入驻申请（个人）")
    @NoRepeatWebLog
    public Result personalCheck(@RequestBody @Validated CereShopPersonalParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.findByShopName(param.getShopName());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.findByPhone(param.getShopPhone());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        CerePlatformBusiness business=cerePlatformBusinessService.checkUserName(param.getShopPhone());
        if(business!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        CerePlatformBusiness business2=cerePlatformBusinessService.checkPhone(param.getShopPhone());
        if(business2!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        String name="";
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
            name=user.getWechatName();
        }
        cereShopPersonalService.personalCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,name,"入驻申请（个人）", GsonUtil.objectToGson(param));
    }

    /**
     * 入驻申请（个体工商户）
     * @param param
     * @return
     */
    @PostMapping("individualCheck")
    @NoRepeatSubmit
    @ApiOperation(value = "入驻申请（个体工商户）")
    @NoRepeatWebLog
    public Result individualCheck(@RequestBody @Validated CereShopIndividualBusinessesParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.findByShopName(param.getShopName());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.findByPhone(param.getShopPhone());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        CerePlatformBusiness business=cerePlatformBusinessService.checkUserName(param.getShopPhone());
        if(business!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        CerePlatformBusiness business2=cerePlatformBusinessService.checkPhone(param.getShopPhone());
        if(business2!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        String name="";
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
            name=user.getWechatName();
        }
        cereShopIndividualBusinessesService.individualCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,name,"入驻申请（个体工商户）", GsonUtil.objectToGson(param));
    }

    /**
     * 入驻申请（企业）
     * @param param
     * @return
     */
    @PostMapping("enterpriseCheck")
    @NoRepeatSubmit
    @ApiOperation(value = "入驻申请（企业）")
    @NoRepeatWebLog
    public Result enterpriseCheck(@RequestBody @Validated CereShopEnterpriseParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.findByShopName(param.getShopName());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.findByPhone(param.getShopPhone());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        CerePlatformBusiness business=cerePlatformBusinessService.checkUserName(param.getShopPhone());
        if(business!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        CerePlatformBusiness business2=cerePlatformBusinessService.checkPhone(param.getShopPhone());
        if(business2!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        String name="";
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
            name=user.getWechatName();
        }
        cereShopEnterpriseService.enterpriseCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,name,"入驻申请（企业）", GsonUtil.objectToGson(param));
    }

    /**
     * 入驻申请（其他组织）
     * @param param
     * @return
     */
    @PostMapping("organizationsCheck")
    @NoRepeatSubmit
    @ApiOperation(value = "入驻申请（其他组织）")
    @NoRepeatWebLog
    public Result organizationsCheck(@RequestBody @Validated CereShopOtherOrganizationsParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.findByShopName(param.getShopName());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.findByPhone(param.getShopPhone());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        CerePlatformBusiness business=cerePlatformBusinessService.checkUserName(param.getShopPhone());
        if(business!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        CerePlatformBusiness business2=cerePlatformBusinessService.checkPhone(param.getShopPhone());
        if(business2!=null){
            return new Result(CoReturnFormat.PHONE_ALREADY_BIND_SHOP);
        }
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        String name="";
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
            name=user.getWechatName();
        }
        cereShopOtherOrganizationsService.organizationsCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,name,"入驻申请（其他组织）", GsonUtil.objectToGson(param));
    }

    /**
     * 修改入驻申请（个人）
     * @param param
     * @return
     */
    @RequestMapping(value = "updatePersonalCheck", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改入驻申请（个人）")
    @NoRepeatWebLog
    public Result updatePersonalCheck(@RequestBody @Validated CereShopPersonalParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.checkShopIdByName(param.getShopName(),param.getShopId());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.checkShopIdByPhone(param.getShopPhone(),param.getShopId());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopPersonalService.updatePersonalCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改入驻申请（个人）", GsonUtil.objectToGson(param));
    }

    /**
     * 修改入驻申请（个体工商户）
     * @param param
     * @return
     */
    @RequestMapping(value = "updateIndividualCheck", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改入驻申请（个体工商户）")
    @NoRepeatWebLog
    public Result updateIndividualCheck(@RequestBody @Validated CereShopIndividualBusinessesParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.checkShopIdByName(param.getShopName(),param.getShopId());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.checkShopIdByPhone(param.getShopPhone(),param.getShopId());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopIndividualBusinessesService.updateIndividualCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改入驻申请（个体工商户）", GsonUtil.objectToGson(param));
    }

    /**
     * 修改入驻申请（企业）
     * @param param
     * @return
     */
    @RequestMapping(value = "updateEnterpriseCheck", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改入驻申请（企业）")
    @NoRepeatWebLog
    public Result updateEnterpriseCheck(@RequestBody @Validated CereShopEnterpriseParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.checkShopIdByName(param.getShopName(),param.getShopId());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.checkShopIdByPhone(param.getShopPhone(),param.getShopId());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopEnterpriseService.updateEnterpriseCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改入驻申请（企业）", GsonUtil.objectToGson(param));
    }

    /**
     * 修改入驻申请（其他组织）
     * @param param
     * @return
     */
    @RequestMapping(value = "updateOrganizationsCheck", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改入驻申请（其他组织）")
    @NoRepeatWebLog
    public Result updateOrganizationsCheck(@RequestBody @Validated CereShopOtherOrganizationsParam param, HttpServletRequest request) throws CoBusinessException{
        //校验商家名称是否存在
        CerePlatformShop shop=cerePlatformShopService.checkShopIdByName(param.getShopName(),param.getShopId());
        if(shop!=null){
            return new Result(CoReturnFormat.SHOP_NAME_ALREADY);
        }
        //校验手机号是否已注册
        CerePlatformShop platformShop=cerePlatformShopService.checkShopIdByPhone(param.getShopPhone(),param.getShopId());
        if(platformShop!=null){
            return new Result(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOtherOrganizationsService.updateOrganizationsCheck(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改入驻申请（其他组织）", GsonUtil.objectToGson(param));
    }
}
