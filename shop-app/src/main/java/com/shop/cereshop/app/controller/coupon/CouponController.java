/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.coupon;

import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.coupon.*;
import com.shop.cereshop.app.redis.service.api.UserRedisService;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.coupon.CereChannelCouponService;
import com.shop.cereshop.app.service.coupon.CereCommonCouponService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.coupon.PlugRequestParam;
import com.shop.cereshop.commons.domain.coupon.PlugRequestParamVO;
import com.shop.cereshop.commons.domain.tool.CereChannelCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.GsonUtil;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 卡券
 */
@RestController
@RequestMapping("coupon")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "CouponController")
@Api(value = "卡券", tags = "卡券")
public class CouponController {

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

    @Autowired
    private CereChannelCouponService cereChannelCouponService;

    @Autowired
    private CereCommonCouponService cereCommonCouponService;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private WxCardUtil wxCardUtil;

    /**
     * 领取优惠券
     * @param param
     * @return
     */
    @PostMapping("takeCoupon")
    @ApiOperation(value = "领取优惠券")
    @NoRepeatWebLog
    public Result takeCoupon(@RequestBody CouponParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        if (param.getSource() == null) {
            param.setSource(IntegerEnum.COUPON_SOURCE_TAKE.getCode());
        }
        cereBuyerCouponService.takeCoupon(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"领取优惠券", GsonUtil.objectToGson(param));
    }

    /**
     * 批量领取优惠券
     * @param paramList
     * @return
     */
    @PostMapping("takeBatchCoupon")
    @ApiOperation(value = "批量领取优惠券")
    @NoRepeatWebLog
    public Result<Void> takeBatchCoupon(@RequestBody List<CouponParam> paramList, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        if (CollectionUtils.isNotEmpty(paramList)) {
            cereBuyerCouponService.takeBatchCoupon(paramList, user);
        }
        return new Result<>(CoReturnFormat.SUCCESS, user.getBuyerUserId().toString(),"批量领取优惠券", null);
    }

    /**
     * 我的卡券列表
     * @return
     */
    @GetMapping("getCoupons")
    @ApiOperation(value = "我的卡券列表")
    public Result<List<ProductCoupon>> getPageCoupons(HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<ProductCoupon> list=cereBuyerCouponService.getCoupons(user.getBuyerUserId());
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 我的卡券列表（不带分页）
     * @param param
     * @return
     */
    @GetMapping("getCouponList")
    @ApiOperation(value = "我的卡券列表")
    public Result<List<ProductCoupon>> getCoupons(CouponListParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<ProductCoupon> list=cereBuyerCouponService.getCouponList(param,user);
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 结算页优惠券列表查询
     * @param param
     * @return
     */
    @GetMapping("getOrderCoupons")
    @ApiOperation(value = "结算页优惠券列表查询")
    public Result<List<ProductCoupon>> getOrderCoupons(OrderCouponParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<ProductCoupon> list = cereBuyerCouponService.getOrderCoupons(param,user);
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 优惠券商品列表查询
     * @return
     */
    @GetMapping("getCouponProducts")
    @ApiOperation(value = "优惠券商品列表查询")
    public Result<Page<Product>> getCouponProducts(ActivityParam param) throws CoBusinessException {
        Page page = cereBuyerCouponService.getCouponProducts(param);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询渠道优惠券详情
     * @return
     */
    @GetMapping("getChannelCouponDetail")
    @ApiOperation(value = "查询渠道优惠券详情")
    public Result<ChannelCouponDetail> getChannelCouponDetail(CereChannelCoupon param, HttpServletRequest request) {
        Long buyerUserId = null;
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            buyerUserId = userRedisService.getBuyerUserIdByToken(token);
        }
        ChannelCouponDetail result = cereChannelCouponService.getChannelCouponDetail(param, buyerUserId);
        return new Result(result);
    }

    @GetMapping("getCouponCenterList")
    @ApiOperation("查询领券中心")
    public Result<Page<CommonCoupon>> getCouponCenterList(PageParam param, HttpServletRequest request) {
        Long buyerUserId = null;
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            buyerUserId = userRedisService.getBuyerUserIdByToken(token);
        }
        Page<CommonCoupon> page = cereCommonCouponService.getCouponCenterList(param, buyerUserId);
        return new Result<>(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 为小程序发券插件构建请求参数
     *
     * @param stockIds 批次id
     * @return
     */
    @PostMapping("/buildParam4Plug")
    @ApiOperation("为小程序发券插件构建请求参数")
    public Result<PlugRequestParamVO> buildParam4Plug(@RequestBody List<String> stockIds) {
        PlugRequestParamVO vo = wxCardUtil.buildParam4Plug(stockIds);
        return new Result<>(vo);
    }

    @PostMapping("/buildParam4PlugList")
    @ApiOperation("批量为小程序发券构建请求参数")
    public Result<List<PlugRequestParamVO>> buildParam4PlugList(@RequestBody List<PlugRequestParam> paramDtos) {
        List<PlugRequestParamVO> list = wxCardUtil.buildParam4PlugList(paramDtos);
        return new Result<>(list);
    }

    /**
     * 查询渠道活动券
     * @param param
     * @return
     * @throws CoBusinessException
     */
    @GetMapping("getChannelActivityCoupon")
    @ApiOperation(value = "查询渠道活动券")
    public Result<List<CommonCoupon>> getChannelActivityCoupon(ActivityParam param, HttpServletRequest request) throws CoBusinessException {
        Long buyerUserId = null;
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            buyerUserId = userRedisService.getBuyerUserIdByToken(token);
        }
        List<CommonCoupon> list = cereCommonCouponService.getChannelActivityCoupon(param, buyerUserId);
        return new Result(list,CoReturnFormat.SUCCESS);
    }


    /**
     * 批量领取渠道优惠券
     * @param param
     * @return
     */
    @PostMapping("takeChannelActivityCoupon")
    @ApiOperation(value = "批量领取渠道优惠券")
    @NoRepeatWebLog
    public Result takeChannelActivityCoupon(@RequestBody ActivityParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Long shopChannelActivityId = param.getActivityId();
        cereCommonCouponService.takeChannelActivityCoupon(shopChannelActivityId, user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"一键领取渠道优惠券", GsonUtil.objectToGson(param));
    }
}
