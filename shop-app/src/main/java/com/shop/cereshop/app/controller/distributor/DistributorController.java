/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.distributor;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.distributor.*;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ShareProduct;
import com.shop.cereshop.app.page.shop.Extension;
import com.shop.cereshop.app.param.distributor.DistributorOrderParam;
import com.shop.cereshop.app.param.distributor.DistributorParam;
import com.shop.cereshop.app.param.distributor.ShopDistributorParam;
import com.shop.cereshop.app.param.extension.ExtensionParam;
import com.shop.cereshop.app.param.product.ShareParam;
import com.shop.cereshop.app.param.shop.ShopParam;
import com.shop.cereshop.app.service.buyer.CereBuyerWithdrawalService;
import com.shop.cereshop.app.service.distributor.CereDistributionOrderService;
import com.shop.cereshop.app.service.distributor.CereShopDistributionLevelService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
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
 * 账户模块
 */
@RestController
@RequestMapping("distributor")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "DistributorController")
@Api(value = "账户模块", tags = "账户模块")
public class DistributorController {

    @Autowired
    private CereDistributionOrderService cereDistributionOrderService;

    @Autowired
    private CereBuyerWithdrawalService cereBuyerWithdrawalService;

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereShopDistributorService cereShopDistributorService;

    @Autowired
    private CereShopDistributionLevelService cereShopDistributionLevelService;

    /**
     * 账户信息查询
     * @return
     */
    @GetMapping("getDistributor")
    @ApiOperation(value = "账户信息查询")
    public Result<Distributor> getDistributor(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Distributor distributor=cereDistributionOrderService.getDistributor(user.getBuyerUserId());
        return new Result(distributor,CoReturnFormat.SUCCESS);
    }

    /**
     * 提现申请
     * @return
     */
    @PostMapping("save")
    @NoRepeatSubmit
    @ApiOperation(value = "提现申请")
    @NoRepeatWebLog
    public Result save(@RequestBody CereBuyerWithdrawal withdrawal, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerWithdrawalService.save(withdrawal,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"提现申请", GsonUtil.objectToGson(withdrawal));
    }

    /**
     * 分销中心列表查询
     * @return
     */
    @GetMapping("getDistributorAll")
    @ApiOperation(value = "分销中心列表查询")
    public Result<Page<DistributorShop>> getDistributorAll(PageParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereDistributionOrderService.getDistributorAll(param,user.getBuyerUserId());
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 店铺分销数据查询
     * @return
     */
    @GetMapping("getShopDistributor")
    @ApiOperation(value = "店铺分销数据查询")
    public Result<ShopDis> getShopDistributor(DistributorParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        ShopDis shopDis=cereDistributionOrderService.getShopDistributor(param,user);
        return new Result(shopDis,CoReturnFormat.SUCCESS);
    }

    /**
     * 累计分销员查询
     * @return
     */
    @GetMapping("getDistributors")
    @ApiOperation(value = "累计分销员查询")
    public Result<Page<CumulativeDistributor>> getDistributors(DistributorOrderParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereDistributionOrderService.getDistributors(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 累计奖励查询
     * @return
     */
    @GetMapping("getReward")
    @ApiOperation(value = "累计奖励查询")
    public Result<Reward> getReward(DistributorOrderParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Reward reward=cereDistributionOrderService.getReward(param,user);
        return new Result(reward,CoReturnFormat.SUCCESS);
    }

    /**
     * 累计客户查询
     * @return
     */
    @GetMapping("getBuyers")
    @ApiOperation(value = "累计客户查询")
    public Result<Page<CumulativeBuyer>> getBuyers(DistributorParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereDistributionOrderService.getBuyers(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 累计未结算奖励查询
     * @return
     */
    @GetMapping("getNotReward")
    @ApiOperation(value = "累计未结算奖励查询")
    public Result<Reward> getNotReward(DistributorOrderParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Reward reward=cereDistributionOrderService.getNotReward(param,user);
        return new Result(reward,CoReturnFormat.SUCCESS);
    }

    /**
     * 店铺推广数据查询
     * @return
     */
    @GetMapping("getShopExtension")
    @ApiOperation(value = "店铺推广数据查询")
    public Result<Extension> getShopExtension(ExtensionParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Extension extension=cereDistributionOrderService.getShopExtension(param,user);
        return new Result(extension,CoReturnFormat.SUCCESS);
    }

    /**
     * 推广商品查询
     * @return
     */
    @GetMapping("getExtensionProduct")
    @ApiOperation(value = "推广商品查询")
    public Result<Page<Product>> getExtensionProduct(ShopParam param) throws CoBusinessException{
        Page page= cerePlatformShopservice.getExtensionProduct(param);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 分享商品
     * @return
     */
    @GetMapping("share")
    @NoRepeatSubmit
    @ApiOperation(value = "分享商品")
    @NoRepeatWebLog
    public Result<ShareProduct> share(ShareParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        CereBuyerUser user= (CereBuyerUser) request.getAttribute("user");
        ShareProduct share =cereShopProductService.share(param,user);
        return new Result(share,CoReturnFormat.SUCCESS,user.getWechatName(),"分享商品", GsonUtil.objectToGson(param));
    }

    /**
     * 分销订单查询
     * @return
     */
    @GetMapping("getDistributorOrder")
    @ApiOperation(value = "分销订单查询")
    public Result<Page<DistributorOrder>> getDistributorOrder(DistributorOrderParam param) throws CoBusinessException{
        Page page =cereDistributionOrderService.getDistributorOrderByShopId(param);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 申请分销员
     * @return
     */
    @PostMapping("addDistributor")
    @ApiOperation(value = "申请分销员")
    @NoRepeatWebLog
    public Result addDistributor(@RequestBody ShopDistributorParam param,HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopDistributorService.addDistributor(param);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"申请分销员", GsonUtil.objectToGson(param));
    }

    /**
     * 绑定关系
     * @return
     */
    @PostMapping("bind")
    @ApiOperation(value = "bind")
    @NoRepeatWebLog
    public Result bind(@RequestBody ExtensionParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopDistributorService.bind(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"绑定关系", GsonUtil.objectToGson(param));
    }

    /**
     * 验证当前客户是否为分销员
     * @return
     */
    @GetMapping("check")
    @NoRepeatSubmit
    @ApiOperation(value = "验证当前客户是否为分销员")
    public Result<CereShopDistributor> check(ShopParam param, HttpServletRequest request) throws CoBusinessException{
        CereBuyerUser user= (CereBuyerUser) request.getAttribute("user");
        CereShopDistributor distributor =cereShopProductService.check(param.getShopId(),user);
        return new Result(distributor,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询分销等级配置
     * @return
     */
    @GetMapping("getDistributionLevelConfig")
    @NoRepeatSubmit
    @ApiOperation(value = "查询分销等级配置")
    public Result<List<LevelDescDTO>> getDistributionLevelConfig(ShopParam param) throws CoBusinessException{
        List<LevelDescDTO> levelList =cereShopDistributionLevelService.getDistributionLevelConfig(param.getShopId());
        return new Result(levelList,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询邀请码
     * @return
     */
    @GetMapping("getInvitationCode")
    @NoRepeatSubmit
    @ApiOperation(value = "查询邀请码")
    public Result<String> getInvitationCode(ShopParam param, HttpServletRequest request) throws CoBusinessException{
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String invitationCode = cereShopDistributorService.getInvitationCode(param.getShopId(), user.getPhone());
        Result<String> result = new Result<>();
        result.setData(invitationCode);
        return result;
    }
}
