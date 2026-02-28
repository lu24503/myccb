/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.seckill.CereShopSeckillDAO;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.seckill.*;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.param.seckill.ProductProblemParam;
import com.shop.cereshop.app.param.seckill.SeckillParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessSeckill;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.buyer.CereBuyerCommentLikeService;
import com.shop.cereshop.app.service.buyer.CereBuyerSeckillVisitService;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.collect.CereBuyerCollectService;
import com.shop.cereshop.app.service.collect.CereBuyerFootprintService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.product.CereCommentWordService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.seckill.CereProductAnswerService;
import com.shop.cereshop.app.service.seckill.CereProductProblemService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillDetailService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSeckillVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.tool.CereShopSeckill;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CereShopSeckillServiceImpl implements CereShopSeckillService {

    @Autowired
    private CereShopSeckillDAO cereShopSeckillDAO;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereShopCartService cereShopCartService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CereShopCommentService cereShopCommentService;

    @Autowired
    private CereCommentWordService cereCommentWordService;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Autowired
    private CereShopCouponService cereShopCouponService;

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CereProductProblemService cereProductProblemService;

    @Autowired
    private CereProductAnswerService cereProductAnswerService;

    @Autowired
    private CereBuyerCollectService cereBuyerCollectService;

    @Autowired
    private CereBuyerFootprintService cereBuyerFootprintService;

    @Autowired
    private CereBuyerCommentLikeService cereBuyerCommentLikeService;

    @Autowired
    private CereBuyerSeckillVisitService cereBuyerSeckillVisitService;

    @Autowired
    private CereShopSeckillDetailService cereShopSeckillDetailService;

    @Override
    public SeckillkIndex getIndex(SeckillParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        SeckillkIndex index=null;
        if(!EmptyUtils.isLongEmpty(param.getShopId())){
            //店铺进来的,查询店铺数据
            index=cereShopSeckillDAO.findShop(param.getShopId());
            //查询秒杀活动数据
            CereShopSeckill cereShopSeckill=cereShopSeckillDAO.selectByPrimaryKey(param.getShopSeckillId());
            //查询购物车数量
            if(user!=null){
                index.setNumber(cereShopCartService.findNumber(user.getBuyerUserId()));
                if(cereShopSeckill!=null){
                    //新增浏览记录
                    CereBuyerSeckillVisit cereBuyerSeckillVisit=new CereBuyerSeckillVisit();
                    cereBuyerSeckillVisit.setBuyerUserId(user.getBuyerUserId());
                    cereBuyerSeckillVisit.setShopSeckillId(cereShopSeckill.getShopSeckillId());
                    cereBuyerSeckillVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                    cereBuyerSeckillVisitService.insert(cereBuyerSeckillVisit);
                }
            }
            //封装预热时间或截止时间倒计时
            if(cereShopSeckill!=null){
                if(cereShopSeckill.getState()==0&&cereShopSeckill.getIfEnable()==2){
                    String nowTime = TimeUtils.yyMMddHHmmss();
                    String moreHourAfter = TimeUtils.getMoreHourAfter(nowTime, cereShopSeckill.getEnableTime());
                    if(TimeUtils.compareTo(moreHourAfter,cereShopSeckill.getEffectiveStart())){
                        index.setEnableTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopSeckill.getEffectiveStart()));
                    }else{
                        cereShopSeckill.setIfEnable(1);
                    }
                }
                if(cereShopSeckill.getState()==1){
                    //设置活动截止倒计时
                    index.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopSeckill.getEffectiveEnd()));
                    index.setState(cereShopSeckill.getState());
                }
            }
        }else {
            //平台进来的
            index=new SeckillkIndex();
        }
        //查询平台秒杀专区商品数据
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<ToolProduct> list=cereShopSeckillDAO.findProducts(param);
        if(!EmptyUtils.isEmpty(list)){
            //设置付款人数
            list.forEach(product -> {
                product.setUsers(cerePlatformShopservice.findPayUsers(product.getProductId()));
                product.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode());
            });
        }
        PageInfo<ToolProduct> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        index.setPage(page);
        //查询分类层级数据
        index.setClassifies(cereShopProductService.getClassify());
        return index;
    }

    @Override
    public ProductDetail getById(CereBuyerUser user, ProductParam param, ProductDetail detail) throws CoBusinessException,Exception {
        /*String time =TimeUtils.yyMMddHHmmss();
        //查询秒杀活动数据
        CereShopSeckill cereShopSeckill=cereShopSeckillDAO.selectByPrimaryKey(param.getShopSeckillId());
        if(cereShopSeckill!=null){
            if(detail!=null){
                if(user!=null){
                    //查询该商品收藏id
                    CereBuyerCollect collect=cereBuyerCollectService.findByUserProduct(user.getBuyerUserId(),detail.getProductId());
                    if(collect!=null){
                        detail.setCollectId(collect.getCollectId());
                        detail.setIfCollect(collect.getState());
                    }
                }
                //判断活动是否开启预热
                detail.setStartTime(cereShopSeckill.getEffectiveStart());
                if(IntegerEnum.ENABLE_START.getCode().equals(cereShopSeckill.getIfEnable())
                        &&!EmptyUtils.isEmpty(cereShopSeckill.getEnableTime())){
                    //如果开启活动预热,计算预热几小时前的时间
                    String enableTime=TimeUtils.headDate(cereShopSeckill.getEffectiveStart(),cereShopSeckill.getEnableTime());
                    //判断当前时间是否处于预热时间和活动开始时间之间
                    if(TimeUtils.isBelong(enableTime,cereShopSeckill.getEffectiveStart())){
                        //如果处于,需要展示预热信息
                        detail.setIfEnable(IntegerEnum.YES.getCode());
                    }
                }
                if(IntegerEnum.COUPON_STATE_START.getCode().equals(cereShopSeckill.getState())){
                    //活动进行中
                    //设置秒杀活动倒计时
                    detail.setTime(TimeUtils.getCountDownByTime(time,cereShopSeckill.getEffectiveEnd()));
                    detail.setIfEnable(IntegerEnum.NO.getCode());
                }
                //查询该商品所有规格名
                List<SkuName> names=cereProductSkuService.findSkuNames(param.getProductId());
                if(!EmptyUtils.isEmpty(names)){
                    //查询该商品所有规格值数据
                    List<SkuValue> list=cereProductSkuService.findValuesByProductId(param.getProductId());
                    if(!EmptyUtils.isEmpty(list)){
                        names.forEach(name -> {
                            if(!EmptyUtils.isEmpty(name.getNameCode())){
                                List<SkuValue> values=new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    if(i<0){
                                        i=0;
                                    }
                                    if(list.get(i).getSkuName().equals(name.getSkuName())){
                                        values.add(list.get(i));
                                        list.remove(i);
                                        i--;
                                    }
                                }
                                name.setValues(values);
                            }
                        });
                    }
                }
                detail.setNames(names);
                //查询该商品所有组合规格数据封装到map
                List<Sku> skus=cereProductSkuService.findSeckillSkuByProductId(param.getProductId(),cereShopSeckill.getShopSeckillId());
                if(!EmptyUtils.isEmpty(skus)) {
                    Map<String, Sku> map = new HashMap<>();
                    String image=detail.getImage();
                    skus.forEach(sku -> {
                        sku.setActivityType(detail.getActivityType());
                        if (EmptyUtils.isEmpty(sku.getImage())) {
                            sku.setImage(image);
                        }
                        if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                            sku.setValueCodes("单款项");
                        }
                        map.put(sku.getValueCodes(), sku);
                    });
                    detail.setMap(map);
                }
                //查询商品图片
                detail.setImages(cereShopProductService.findImages(param.getProductId()));
                //查询商品评论信息
                List<BuyerComment> comments=cereShopCommentService.findByProductId(param.getProductId());
                //封装评论图片数组数据
                if(!EmptyUtils.isEmpty(comments)){
                    comments.forEach(buyerComment -> {
                        buyerComment.setImages(EmptyUtils.getImages(buyerComment.getImage()));
                        buyerComment.setAddImages(EmptyUtils.getImages(buyerComment.getAddImage()));
                        buyerComment.setValues(EmptyUtils.getImages(buyerComment.getValue()));
                        //设置是否点赞
                        if(user!=null){
                            CereBuyerCommentLike cereBuyerCommentLike=cereBuyerCommentLikeService.findByUserIdAndCommentId(user.getBuyerUserId(),buyerComment.getCommentId());
                            if(cereBuyerCommentLike!=null){
                                buyerComment.setIfLike(cereBuyerCommentLike.getIfLike());
                            }
                        }
                    });
                    //查询该商品评论关联关键词统计数据
                    List<CommentWord> words=cereCommentWordService.findByProductId(param.getProductId());
                    detail.setWords(words);
                }
                detail.setComments(comments);
                //查询店铺商品总类数
                detail.setClassifyNumber(cereShopProductService.findClassifyNumber(param.getShopId()).size());
                if(IntegerEnum.YES.getCode().equals(cereShopSeckill.getIfAdd())){
                    String couponSplicing="";
                    //如果优惠券允许叠加,查询平台优惠券
                    List<ProductCoupon> tools=null;
                    if(user!=null){
                        //过滤已使用和已过期的优惠券
                        tools=cerePlatformActivityService.findCouponByProductIdAndUserId(user.getBuyerUserId(),param.getProductId());
                        tools=tools.stream()
                                .filter(productCoupon -> IntegerEnum.COUPON_NOT_HAVE.getCode().equals(productCoupon.getState())
                                        ||IntegerEnum.COUPON_HAVE.getCode().equals(productCoupon.getState()))
                                .peek(productCoupon -> {
                                    if(!EmptyUtils.isEmpty(productCoupon.getFrequency())){
                                        //如果有限制领取次数
                                        if(productCoupon.getFrequency().equals(productCoupon.getCount())){
                                            //如果限制领取次数=用户已领取优惠券总数,修改店铺优惠券状态为已领取
                                            productCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                                        }
                                    }
                                }).collect(Collectors.toList());
                    }else {
                        tools=cerePlatformActivityService.findCouponByProductId(param.getProductId());
                    }
                    detail.setMarkTools(tools);
                    //设置活动图片数组
                    if(!EmptyUtils.isEmpty(tools)){
                        detail.setCouponImages(tools.stream().map(ProductCoupon::getImage).distinct().collect(Collectors.toList()));
                    }
                    //然后查询店铺优惠券
                    List<ProductCoupon> shopCoupons=null;
                    if(user!=null){
                        shopCoupons=cereShopCouponService.findByProductIdAndUserId(user.getBuyerUserId(),param.getProductId());
                        if(!EmptyUtils.isEmpty(shopCoupons)){
                            //设置优惠券状态
                            shopCoupons.forEach(shopCoupon -> {
                                //查询用户已领取的优惠券总数
                                if(!EmptyUtils.isEmpty(shopCoupon.getFrequency())){
                                    //如果有限制领取次数
                                    if(shopCoupon.getFrequency().equals(shopCoupon.getCount())){
                                        //如果限制领取次数=用户已领取优惠券总数,修改店铺优惠券状态为已领取
                                        shopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                                    }
                                }
                            });
                        }
                    }else {
                        shopCoupons=cereShopCouponService.findByProductId(param.getProductId());
                    }
                    detail.setShopMarkTools(shopCoupons);
                    if(!EmptyUtils.isEmpty(tools)){
                        //设置优惠券内容
                        tools.forEach(tool -> {
                            tool.setContent("满"+tool.getFullMoney().stripTrailingZeros().toPlainString()+
                                    "减"+tool.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                        });
                        couponSplicing=tools.stream().map(tool -> "满"+tool.getFullMoney().stripTrailingZeros().toPlainString()
                                +"减"+tool.getReduceMoney().stripTrailingZeros().toPlainString()).collect(Collectors.joining(";"));
                    }
                    if(!EmptyUtils.isEmpty(shopCoupons)){
                        shopCoupons.forEach(tool -> {
                            if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(tool.getCouponType())){
                                tool.setContent("满"+tool.getFullMoney().stripTrailingZeros().toPlainString()+
                                        "减"+tool.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                            }else {
                                tool.setContent(tool.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                            }
                        });
                        couponSplicing+=shopCoupons.stream().map(tool ->
                                IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(tool.getCouponType())?
                                        "满"+tool.getFullMoney().stripTrailingZeros().toPlainString()
                                                +"减"+tool.getReduceMoney().stripTrailingZeros().toPlainString():
                                        tool.getReduceMoney().stripTrailingZeros().toPlainString()+"折券").collect(Collectors.joining(";"));
                    }
                    detail.setCouponSplicing(couponSplicing);
                }
                //查询同类商品
                List<Product> similarProducts = cereShopProductService.findSimilarProducts(detail.getClassifyId());
                detail.setSimilarProducts(similarProducts);
                return detail;
            }
        }*/
        return null;
    }

    @Override
    public Page getProblems(ProductProblemParam param,CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        //查询该商品所有提问
        List<SeckillProductProblem> list=cereProductProblemService.getProblems(param.getProductId());
        if(!EmptyUtils.isEmpty(list)){
            List<Long> ids=null;
            if(user!=null){
                //查询当前登录用户是否购买过该商品
                ids=cereProductProblemService.findOrderByUserProductId(param.getProductId(),user.getBuyerUserId());
            }
            List<Long> finalIds = ids;
            list.forEach(problem -> {
                //设置回答明细数据
                List<SeckillProductAnswer> answerList = cereProductAnswerService.findAnswersById(problem.getProblemId());
                problem.setAnswers(answerList);
                //设置默认不展示回答按钮
                List<Long> answerBuyerUserIdList = answerList.stream().map(SeckillProductAnswer::getBuyerUserId).collect(Collectors.toList());
                problem.setIfAnswer(IntegerEnum.NO.getCode());
                if(!EmptyUtils.isEmpty(finalIds)){
                    //买过该商品,判断当前提问数据是否是当前登录人提交的
                    //因为上面判断了finalIds不为空，finalIds 来自于 ids 意味着user != null
                    if(!problem.getBuyerUserId().equals(user.getBuyerUserId()) && !answerBuyerUserIdList.contains(user.getBuyerUserId())){
                        //不是,展示按钮
                        problem.setIfAnswer(IntegerEnum.YES.getCode());
                    }
                }
            });
        }
        PageInfo<SeckillProductProblem> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public CereShopSeckill findById(Long shopSeckillId) {
        return cereShopSeckillDAO.selectByPrimaryKey(shopSeckillId);
    }

    @Override
    public Long findByProductId(Long skuId) {
        return cereShopSeckillDAO.findSeckillIdBySkuId(skuId);
    }

    @Override
    public SeckillAnswerDetail getProblemDetail(Long problemId,CereBuyerUser user) throws CoBusinessException {
        //查询提问数据
        SeckillAnswerDetail detail=cereProductProblemService.findProblemDetail(problemId);
        if(detail!=null){
            //设置默认展示回答输入框
            detail.setIfExhibition(IntegerEnum.YES.getCode());
            //查询是否是当前用户提问数据
            if(user!=null && user.getBuyerUserId().equals(detail.getBuyerUserId())){
                //如果是,不展示回答输入框
                detail.setIfExhibition(IntegerEnum.NO.getCode());
            }
            if(detail.getProblemId() != null){
                //设置回答明细数据
                List<SeckillProductAnswer> answerList = cereProductAnswerService.findAnswersById(detail.getProblemId());
                List<Long> answerBuyerUserIdList = answerList.stream().map(SeckillProductAnswer::getBuyerUserId).collect(Collectors.toList());
                if (user != null && answerBuyerUserIdList.contains(user.getBuyerUserId())) {
                    detail.setIfExhibition(IntegerEnum.NO.getCode());
                }
                detail.setAnswers(answerList);
            }
        }
        return detail;
    }

    @Override
    public List<ShopSeckillDetail> getSeckills(RenovationParam param) {
        List<ShopSeckillDetail> list=cereShopSeckillDAO.getSeckills(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(detail -> {
                //查询商品明细
                detail.setProducts(cereShopSeckillDetailService.findDistinctProducts(detail.getShopSeckillId()));
            });
        }
        return list;
    }

    @Override
    public List<ShopBusinessSeckill> selectByShopIdList(List<Long> shopIdList) {
        return cereShopSeckillDAO.selectByShopIdList(shopIdList);
    }

    @Override
    public ProductDetail setActivityInfo(Long shopSeckillId, CereBuyerUser user, ProductParam param, ProductDetail detail) {
        try {
            String time = TimeUtils.yyMMddHHmmss();
            //查询秒杀活动数据
            CereShopSeckill cereShopSeckill=null;
            Long activitySkuId = param.getSkuId();
            if (shopSeckillId != null && shopSeckillId > 0) {
                cereShopSeckill = cereShopSeckillDAO.selectByPrimaryKey(shopSeckillId);
            } else {
                cereShopSeckill = cereShopSeckillDAO.selectByProductId(detail.getProductId());
                activitySkuId = cereShopSeckillDetailService.findSkuIdByProductId(detail.getProductId());
            }
            if(cereShopSeckill!=null&&(IntegerEnum.ENABLE_START.getCode().equals(cereShopSeckill.getIfEnable())
                    ||IntegerEnum.COUPON_STATE_START.getCode().equals(cereShopSeckill.getState()))){
                shopSeckillId = cereShopSeckill.getShopSeckillId();
                detail.setShopSeckillId(shopSeckillId);
                detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode());
                detail.setIfAdd(cereShopSeckill.getIfAdd());
                detail.setStartTime(cereShopSeckill.getEffectiveStart());
                detail.setEndTime(cereShopSeckill.getEffectiveEnd());
                //判断活动是否开启预热
                if(IntegerEnum.ENABLE_START.getCode().equals(cereShopSeckill.getIfEnable())
                        &&!EmptyUtils.isEmpty(cereShopSeckill.getEnableTime())){
                    //如果开启活动预热,计算预热几小时前的时间
                    String enableTime=TimeUtils.headDate(cereShopSeckill.getEffectiveStart(),cereShopSeckill.getEnableTime());
                    //判断当前时间是否处于预热时间和活动开始时间之间
                    if(TimeUtils.isBelong(enableTime,cereShopSeckill.getEffectiveStart())){
                        //如果处于,需要展示预热信息
                        detail.setIfEnable(IntegerEnum.YES.getCode());
                    }
                }
                if(IntegerEnum.COUPON_STATE_START.getCode().equals(cereShopSeckill.getState())){
                    //活动进行中
                    //设置秒杀活动倒计时
                    detail.setTime(TimeUtils.getCountDownByTime(time,cereShopSeckill.getEffectiveEnd()));
                    detail.setIfEnable(IntegerEnum.NO.getCode());
                }

                if(user!=null){
                    //新增浏览记录
                    CereBuyerSeckillVisit cereBuyerSeckillVisit=new CereBuyerSeckillVisit();
                    cereBuyerSeckillVisit.setBuyerUserId(user.getBuyerUserId());
                    cereBuyerSeckillVisit.setShopSeckillId(shopSeckillId);
                    cereBuyerSeckillVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                    cereBuyerSeckillVisitService.insert(cereBuyerSeckillVisit);
                }

                //设置库存总数 剩余库存数 原始价格 当前价格
                ProductStockInfo stockInfo = cereShopSeckillDetailService.selectSkuStockInfo(shopSeckillId, activitySkuId);
                if (stockInfo != null && stockInfo.getTotal() != null && stockInfo.getStockNumber() != null) {
                    detail.setTotal(stockInfo.getTotal());
                    detail.setSurplusNumber(stockInfo.getStockNumber());
                }
                detail.setOriginalPrice(detail.getPrice());
                detail.setPrice(cereShopSeckillDetailService.findPriceBySeckillIdAndSkuId(shopSeckillId, activitySkuId));

                List<Sku> skus = cereProductSkuService.findSeckillSkuByProductId(param.getProductId(),cereShopSeckill.getShopSeckillId());

                //查询该商品所有组合规格数据封装到map
                if(!EmptyUtils.isEmpty(skus)) {
                    Map<String, Sku> map = new HashMap<>();
                    String image=detail.getImage();
                    for (Sku sku:skus) {
                        if (sku.getActivityType() != null && sku.getActivityType() != 0) {
                            sku.setShopSeckillId(shopSeckillId);
                            sku.setStartTime(detail.getStartTime());
                            sku.setEndTime(detail.getEndTime());
                            sku.setTime(detail.getTime());
                            sku.setIfEnable(detail.getIfEnable());
                        }
                        if (EmptyUtils.isEmpty(sku.getImage())) {
                            sku.setImage(image);
                        }
                        if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                            sku.setValueCodes("单款项");
                        }
                        map.put(sku.getValueCodes(), sku);
                    };
                    detail.setMap(map);
                }
            }
        } catch (Exception e) {
            log.error("setActivtyInfo shopSeckillId = {}, productId = {}, errorMessage = {}", shopSeckillId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }

    @Override
    public ProductDetail setActivityInfoForRealInfo(Long shopSeckillId, CereBuyerUser user, ProductParam param, ProductDetail detail) {
        try {
            if (user != null) {
                //新增浏览记录
                CereBuyerSeckillVisit cereBuyerSeckillVisit=new CereBuyerSeckillVisit();
                cereBuyerSeckillVisit.setBuyerUserId(user.getBuyerUserId());
                cereBuyerSeckillVisit.setShopSeckillId(shopSeckillId);
                cereBuyerSeckillVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                cereBuyerSeckillVisitService.insert(cereBuyerSeckillVisit);
            }
            //设置库存总数 剩余库存数 原始价格 当前价格
            ProductStockInfo stockInfo = cereShopSeckillDetailService.selectSkuStockInfo(shopSeckillId, detail.getSkuId());
            if (stockInfo != null && stockInfo.getTotal() != null && stockInfo.getStockNumber() != null) {
                detail.setTotal(stockInfo.getTotal());
                detail.setSurplusNumber(stockInfo.getStockNumber());
            }
        } catch (Exception e) {
            log.error("setActivtyInfo shopSeckillId = {}, productId = {}, errorMessage = {}", shopSeckillId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }
}
