/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.canvas;

import com.shop.cereshop.app.page.MemberProduct;
import com.shop.cereshop.app.page.canvas.CanvasCoupon;
import com.shop.cereshop.app.page.canvas.CanvasPlatformDiscount;
import com.shop.cereshop.app.page.canvas.CanvasPlatformSeckill;
import com.shop.cereshop.app.page.canvas.CanvasProduct;
import com.shop.cereshop.app.page.live.CereLivePage;
import com.shop.cereshop.app.page.notice.BuyerNotice;
import com.shop.cereshop.app.page.price.ShopPriceDetail;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.CanvasAppProductParam;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.member.MemberProductPageParam;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.index.IndexService;
import com.shop.cereshop.app.service.live.CereLiveService;
import com.shop.cereshop.app.service.notice.CereNoticeService;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.app.service.price.CereShopPriceService;
import com.shop.cereshop.app.service.product.CereProductMemberService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.product.Classify;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 画布选择商品查询
 */
@RestController
@RequestMapping("canvas")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "CanvasController")
@Api(value = "画布选择商品查询", tags = "画布选择商品查询")
public class CanvasController {

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private CereShopCouponService cereShopCouponService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CerePlatformSeckillService cerePlatformSeckillService;

    @Autowired
    private CerePlatformDiscountService cerePlatformDiscountService;

    @Autowired
    private CereNoticeService cereNoticeService;

    @Autowired
    private CereProductMemberService cereProductMemberService;

    @Autowired
    private CereShopPriceService cereShopPriceService;

    @Autowired
    private CereLiveService cereLiveService;

    /**
     * 画布选择商品查询
     * @param param
     * @return
     */
    @GetMapping("getProducts")
    @ApiOperation(value = "商家编辑查询")
    public Result<Page<CanvasProduct>> getProducts(CanvasAppProductParam param) throws CoBusinessException,Exception{
        Page page=cereShopProductService.getCanvasProducts(param);
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 画布选择商品查询新接口,如果调用有问题可以切换回getProducts接口
     * @param param
     * @return
     */
    @GetMapping("getProducts2")
    @ApiOperation(value = "商家编辑查询新接口")
    public Result<Page<CanvasProduct>> getProducts2(CanvasAppProductParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Long memberLevelId = null;
        if (user != null) {
            memberLevelId = user.getMemberLevelId();
        }
        param.setMemberLevelId(memberLevelId);
        Page page=cereShopProductService.getProducts2(param);
        return new Result(page, CoReturnFormat.SUCCESS);
    }
    /**
     * 查询分类层级
     * @return
     */
    @GetMapping("getClassify")
    @ApiOperation(value = "查询分类层级")
    public Result<Classify> getClassify() throws CoBusinessException{
        List<Classify> list=cereShopProductService.getClassify();
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询所有平台优惠券
     * @return
     */
    @GetMapping("getCoupons")
    @ApiOperation(value = "查询所有平台优惠券")
    public Result<Page<CanvasCoupon>> getCoupons(CanvasCouponParam param,HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page=cerePlatformActivityService.getCoupons(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 查询所有店铺优惠券
     * @return
     */
    @GetMapping("getShopCoupons")
    @ApiOperation(value = "查询所有店铺优惠券")
    public Result<Page<ProductCoupon>> getShopCoupons(CanvasCouponParam param,HttpServletRequest request) throws CoBusinessException {
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page=cereShopCouponService.getShopCoupons(param,user);
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 读取画布数据
     * @return
     */
    @GetMapping("getCanvas")
    @ApiOperation(value = "读取画布数据")
    public Result getCanvas(CerePlatformCanvas canvas, HttpServletRequest request)  throws CoBusinessException{
        canvas=indexService.getCanvas(canvas);
        return new Result(canvas,CoReturnFormat.SUCCESS);
    }

    /**
     * 选择平台端秒杀活动查询
     * @return
     */
    @GetMapping("getPlatformSeckills")
    @ApiOperation(value = "选择平台端秒杀活动查询")
    public Result<List<CanvasPlatformSeckill>> getPlatformSeckills(RenovationParam param) throws CoBusinessException,Exception{
        List<CanvasPlatformSeckill> list=cerePlatformSeckillService.getPlatformSeckills(param);
        return new Result(list);
    }

    /**
     * 选择平台端限时折扣活动查询
     * @return
     */
    @GetMapping("getMinDiscount")
    @ApiOperation(value = "选择平台端限时折扣活动查询")
    public Result<List<CanvasPlatformDiscount>> getMinDiscount(RenovationParam param) throws CoBusinessException,Exception{
        List<CanvasPlatformDiscount> discounts=cerePlatformDiscountService.getMinDiscount(param);
        return new Result(discounts);
    }

    /**
     * 选择拼团活动查询
     * @return
     */
    @GetMapping("getGroupWorks")
    @ApiOperation(value = "选择拼团活动查询")
    public Result<List<ToolProduct>> getGroupWorks(RenovationParam param) throws CoBusinessException{
        List<ToolProduct> list=cerePlatformActivityService.getGroupWorkProducts(param);
        return new Result(list);
    }

    /**
     * 获取公告数据
     * @return
     */
    @GetMapping("getNotices")
    @ApiOperation(value = "获取公告数据")
    public Result<List<BuyerNotice>> getNotices() throws CoBusinessException,Exception{
        return new Result(cereNoticeService.getNotices(), CoReturnFormat.SUCCESS);
    }

    /**
     * 查询会员商品数据
     * @return
     */
    @GetMapping("getMemberProducts")
    @ApiOperation(value = "查询会员商品数据")
    public Result<Page<MemberProduct>> getMemberProducts(MemberProductPageParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Long memberLevelId = null;
        if (user != null) {
            memberLevelId = user.getMemberLevelId();
        }
        param.setMemberLevelId(memberLevelId);
        Page page=cereProductMemberService.getMemberProducts(param);
        return new Result(page);
    }

    /**
     * 画布选择定价捆绑活动数据
     * @param param
     * @return
     */
    @GetMapping("getPrices")
    @ApiOperation(value = "画布选择定价捆绑活动数据")
    public Result<List<ShopPriceDetail>> getPrices(RenovationParam param) throws CoBusinessException{
        List<ShopPriceDetail> list=cereShopPriceService.getPrices(param);
        return new Result(list);
    }

    /**
     * 查询直播列表
     * @return
     */
    @GetMapping("selectLiveList")
    @ApiOperation(value = "查询直播列表")
    public Result<List<CereLivePage>> selectLiveList(PageParam param, HttpServletRequest request) throws CoBusinessException {
        Long buyerUserId = null;
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        if (user != null) {
            buyerUserId = user.getBuyerUserId();
        }
        Page<CereLivePage> list = cereLiveService.selectLiveList(buyerUserId, param);
        return new Result(list);
    }
}
