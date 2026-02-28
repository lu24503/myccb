/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork.impl;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.groupwork.CereShopGroupWorkDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.groupwork.GroupInvite;
import com.shop.cereshop.app.page.groupwork.GroupWorkIndex;
import com.shop.cereshop.app.page.groupwork.ShareQrcode;
import com.shop.cereshop.app.page.groupwork.ShopGroupWorkUDetail;
import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.settlement.Distribution;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.groupwork.CollageIdParam;
import com.shop.cereshop.app.param.groupwork.GroupWorkParam;
import com.shop.cereshop.app.param.groupwork.GroupWorkSettlementParam;
import com.shop.cereshop.app.param.groupwork.ShareQrcodeParam;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.buyer.CereBuyerCommentLikeService;
import com.shop.cereshop.app.service.buyer.CereBuyerReceiveService;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.collect.CereBuyerCollectService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderDetailService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkDetailService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkService;
import com.shop.cereshop.app.service.logistics.CereOrderLogisticsService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.app.service.product.CereCommentWordService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.DictConstants;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.collage.CollageOrder;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWork;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.StringUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

@Slf4j
@Service
public class CereShopGroupWorkServiceImpl implements CereShopGroupWorkService {

    @Autowired
    private CereShopGroupWorkDAO cereShopGroupWorkDAO;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereShopCartService cereShopCartService;

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
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CereShopCouponService cereShopCouponService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CereCollageOrderService cereCollageOrderService;

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @Autowired
    private CereOrderLogisticsService cereOrderLogisticsService;

    @Autowired
    private CereBuyerReceiveService cereBuyerReceiveService;

    @Autowired
    private CereBuyerCollectService cereBuyerCollectService;

    @Autowired
    private CereBuyerCommentLikeService cereBuyerCommentLikeService;

    @Autowired
    private CereCollageOrderDetailService cereCollageOrderDetailService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private CereShopGroupWorkDetailService cereShopGroupWorkDetailService;

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private WxMaQrcodeService wxMaQrcodeService;

    /**
     * 支付小程序appid
     */
    @Value("${weixin.appid}")
    private String appId;

    /**
     * 支付小程序秘钥
     */
    @Value("${weixin.secret}")
    private String secret;

    @Value("${spring.domain}")
    private String domain;

    @Override
    public GroupWorkIndex getIndex(GroupWorkParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        GroupWorkIndex index=null;
        if(!EmptyUtils.isLongEmpty(param.getShopId())){
            //店铺进来的,查询店铺数据
            index=cereShopGroupWorkDAO.findShop(param.getShopId());
            //查询拼团活动数据
            CereShopGroupWork cereShopGroupWork=cereShopGroupWorkDAO.selectByPrimaryKey(param.getShopGroupWorkId());
            if(cereShopGroupWork!=null){

                if(cereShopGroupWork.getState()==0&&cereShopGroupWork.getIfEnable()==2){
                    String nowTime = TimeUtils.yyMMddHHmmss();
                    String moreHourAfter = TimeUtils.getMoreHourAfter(nowTime, cereShopGroupWork.getEnableTime());
                    if(TimeUtils.compareTo(moreHourAfter,cereShopGroupWork.getStartTime())){
                        index.setEnableTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopGroupWork.getStartTime()));
                    }else{
                        cereShopGroupWork.setIfEnable(1);
                    }
                }
                if(cereShopGroupWork.getState()==1){
                    //设置活动截止倒计时
                    index.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopGroupWork.getEndTime()));
                    index.setState(cereShopGroupWork.getState());
                }
                //计算活动倒计时
                index.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopGroupWork.getEndTime()));
            }
        }else {
            //平台进来的
            index=new GroupWorkIndex();
            index.setState(1);
        }
        //查询拼团专区商品数据
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<ToolProduct> list=cereShopGroupWorkDAO.findProducts(param);
        if(!EmptyUtils.isEmpty(list)){
            //设置付款人数
            list.forEach(product -> {
                product.setUsers(cerePlatformShopservice.findPayUsers(product.getProductId()));
                product.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_GROUP.getCode());
            });
        }
        PageInfo<ToolProduct> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        index.setPage(page);
        //查询分类层级数据
        index.setClassifies(cereShopProductService.getClassify());
        //查询购物车数量
        if(user!=null){
            index.setNumber(cereShopCartService.findNumber(user.getBuyerUserId()));
        }
        return index;
    }

    @Override
    public ProductDetail getById(CereBuyerUser user, ProductParam param,ProductDetail detail) throws CoBusinessException,Exception {
        /*//查询拼团活动数据
        CereShopGroupWork cereShopGroupWork=cereShopGroupWorkDAO.selectByPrimaryKey(param.getShopGroupWorkId());
        if(cereShopGroupWork!=null){
            if(detail!=null){
                Long buyerUserId=null;
                if(user!=null){
                    buyerUserId=user.getBuyerUserId();
                    //查询该商品收藏id
                    CereBuyerCollect collect=cereBuyerCollectService.findByUserProduct(user.getBuyerUserId(),detail.getProductId());
                    if(collect!=null){
                        detail.setCollectId(collect.getCollectId());
                        detail.setIfCollect(collect.getState());
                    }
                }
                //判断活动是否开启预热
                detail.setStartTime(cereShopGroupWork.getStartTime());
                if(IntegerEnum.ENABLE_START.getCode().equals(cereShopGroupWork.getIfEnable())
                &&!EmptyUtils.isEmpty(cereShopGroupWork.getEnableTime())){
                    //如果开启活动预热,计算预热几小时前的时间
                    String enableTime=TimeUtils.headDate(cereShopGroupWork.getStartTime(),cereShopGroupWork.getEnableTime());
                    //判断当前时间是否处于预热时间和活动开始时间之间
                    if(TimeUtils.isBelong(enableTime,cereShopGroupWork.getStartTime())){
                        //如果处于,需要展示预热信息
                        detail.setIfEnable(IntegerEnum.YES.getCode());
                    }
                }
                if(IntegerEnum.COUPON_STATE_START.getCode().equals(cereShopGroupWork.getState())){
                    //活动进行中
                    //设置活动倒计时
                    detail.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopGroupWork.getEndTime()));
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
                List<Sku> skus=cereProductSkuService.findGroupWorkSkuByProductId(param.getProductId(),cereShopGroupWork.getShopGroupWorkId());
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
                //设置成团人数
                detail.setPerson(cereShopGroupWork.getPerson());
                //查询拼单数据
                List<CollageOrder> collageOrders=cereCollageOrderService.findCollageOrders(param.getShopGroupWorkId(),buyerUserId,param.getProductId());
                if(!EmptyUtils.isEmpty(collageOrders)){
                    for (CollageOrder collage : collageOrders) {
                        //查询该拼单已拼人数
                        int people=cereCollageOrderService.findSpelled(collage.getCollageId());
                        //设置剩余成团人数
                        collage.setPerson(cereShopGroupWork.getPerson()-people);
                        //计算成团有效时间=拼单的发起时间延时拼团活动设置的有效时长后的时间
                        String time = TimeUtils.getMoreHourAfter(collage.getCreateTime(),cereShopGroupWork.getEffectiveTime());
                        //设置成团倒计时时间
                        collage.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),time));
                    }
                    detail.setCollageOrders(collageOrders);
                }
                if(IntegerEnum.YES.getCode().equals(cereShopGroupWork.getIfAdd())){
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
                                        if(productCoupon.getFrequency()==productCoupon.getCount()){
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
                                    if(shopCoupon.getFrequency()==shopCoupon.getCount()){
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
    public Settlement getSettlement(GroupWorkSettlementParam param, CereBuyerUser user) throws CoBusinessException {
        Settlement settlement=new Settlement();
        if(EmptyUtils.isEmpty(param.getReceiveId())){
            //查询默认地址
            settlement.setReceive(cereBuyerReceiveService.findlatelyReceiveByUserId(user.getBuyerUserId()));
        }else {
            settlement.setReceive(cereBuyerReceiveService.findById(param.getReceiveId()));
        }
        List<ProductCoupon> coupons=new ArrayList<>();
        //查询拼团活动数据
        CereShopGroupWork cereShopGroupWork=cereShopGroupWorkDAO.selectByPrimaryKey(param.getShopGroupWorkId());
        if(cereShopGroupWork!=null){
            if(IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopGroupWork.getIfLimit())){
                //如果拼团活动限购,校验数量
                if(param.getNumber()>cereShopGroupWork.getLimitNumber()){
                    throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                }
            }
            List<SettlementShop> shops=new ArrayList<>();
            Long shopId = cereShopGroupWork.getShopId();

            //该sku可以抵扣多少积分
            Map<Long, Integer> skuCreditMap = new HashMap<>();

            //商品还可以抵扣多少积分
            Map<Long, Integer> productRemainCreditMap = new HashMap<>();

            //积分开关
            boolean creditSwitch = false;
            Integer totalRemainCredit = 0;
            //默认1积分抵扣0.01
            BigDecimal creditExchangeRate = new BigDecimal("0.01");
            CerePlatformDict dict = cerePlatformDictService.getByName(DictConstants.CREDIT_SWITCH);
            //一笔订单达到多少金额才能抵扣积分
            CerePlatformDict dict2 = cerePlatformDictService.getByName(DictConstants.CREDIT_ORDER_AMOUNT_THRESHOLD);
            //一笔订单最多抵扣多少积分
            CerePlatformDict dict3 = cerePlatformDictService.getByName(DictConstants.CREDIT_DEDUCT_LIMIT);
            //1积分抵扣多少额度
            CerePlatformDict dict4 = cerePlatformDictService.getByName(DictConstants.CREDIT_EXCHANGE_RATE);
            log.info("dict: {} dict2: {} dict3:{} dict4: {}", JSON.toJSONString(dict), JSON.toJSONString(dict2),
                    JSON.toJSONString(dict3), JSON.toJSONString(dict4));
            if (dict != null && StringUtils.isNotBlank(dict.getDictDescribe())
                    && IntegerEnum.YES.getCode().equals(Integer.parseInt(dict.getDictDescribe()))) {
                creditSwitch = true;
                CereBuyerUser buyerUser = cereBuyerUserService.selectByBuyerUserId(user.getBuyerUserId());
                totalRemainCredit = buyerUser.getCredit();
                settlement.setUserTotalCredit(buyerUser.getCredit());
                settlement.setSkuCreditMap(skuCreditMap);
                BigDecimal threshold = BigDecimal.ZERO;
                try {
                    threshold = new BigDecimal(dict2.getDictDescribe());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                settlement.setOrderCreditThreshold(threshold);
                int deductLimit = 0;
                try {
                    deductLimit = Integer.parseInt(dict3.getDictDescribe());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                settlement.setCreditDeductLimit(deductLimit);
                try {
                    creditExchangeRate = new BigDecimal(dict4.getDictDescribe());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }


            //查询店铺数据
            SettlementShop settlementShop=cereShopOrderService.findSettlementShop(shopId);
            if(settlementShop!=null){
                //查询规格信息数据
                List<CartSku> skus=cereProductSkuService.findSkuBySkuIdAndWorkId(param);
                if(!EmptyUtils.isEmpty(skus)){
                    for (CartSku sku:skus) {
                        if (creditSwitch && IntegerEnum.YES.getCode().equals(sku.getIfCredit())) {
                            productRemainCreditMap.put(sku.getProductId(), sku.getCreditLimit());
                        } else {
                            productRemainCreditMap.put(sku.getProductId(), 0);
                        }
                    }
                    //将商品数量放到map中
                    Map<Long, Integer> map = new HashMap<>();
                    map.put(param.getSkuId(),param.getNumber());
                    //查询店铺物流方案
                    List<CereOrderLogistics> logistics=cereOrderLogisticsService.findLogistics(shopId);
                    //计算物流费用
                    Distribution distribution=cereShopOrderService.setLogisticPrice(logistics,skus,settlement.getReceive(),map);
                    if (distribution == null) {
                        settlementShop.setReceiveNotMatch(true);
                        distribution = new Distribution();
                        distribution.setDistributionName("全国包邮");
                        distribution.setDistributionPrice(BigDecimal.ZERO);
                        distribution.setLogisticsId(-1L);
                        settlementShop.setDistribution(distribution);
                    } else {
                        settlementShop.setDistribution(distribution);
                    }
                    BigDecimal total=BigDecimal.ZERO;
                    for (CartSku sku : skus) {
                        //设置规格属性值数组
                        sku.setValues(EmptyUtils.getImages(sku.getValue()));
                        //设置商品数量
                        sku.setNumber(param.getNumber());
                        //校验库存
                        //int stockNumber=0;
                            /*if(!EmptyUtils.isEmpty(stringRedisService.get(String.valueOf(sku.getSkuId())))){
                                stockNumber=(int) stringRedisService.get(String.valueOf(sku.getSkuId()));
                            }else {
                                stockNumber=cereProductSkuService.findStockNumberBySkuId(sku.getSkuId());
                            }*/
                        int stockNumber=cereProductSkuService.findStockNumberBySkuId(sku.getSkuId());
                        if(sku.getNumber()>stockNumber){
                            //超出库存,提示‘商品已不可售，看看其他的吧’
                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                        }

                        Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                        if (totalRemainCredit > 0 && remainCredit > 0) {
                            BigDecimal price = sku.getPrice().multiply(new BigDecimal(param.getNumber()));
                            int needCredit = price.divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                            if (needCredit > 0) {
                                int min = Math.min(remainCredit, totalRemainCredit);
                                if (min >= needCredit) {
                                    productRemainCreditMap.put(sku.getProductId(), remainCredit - needCredit);
                                    skuCreditMap.put(sku.getSkuId(), needCredit);
                                    totalRemainCredit -= needCredit;
                                } else {
                                    productRemainCreditMap.put(sku.getProductId(), remainCredit - min);
                                    skuCreditMap.put(sku.getSkuId(), min);
                                    totalRemainCredit -= min;
                                }
                            }
                        }

                        //封装商品数据
                        sku.setTotal(new BigDecimal(param.getNumber()).multiply(sku.getPrice()));
                        total=total.add(sku.getTotal());
                        sku.setSelected(IntegerEnum.YES.getCode());
                        if(IntegerEnum.YES.getCode().equals(cereShopGroupWork.getIfAdd())){
                            //如果允许优惠叠加,查询平台优惠券数据
                            //根据商品id、商品价格查询满足条件的优惠券
                            List<ProductCoupon> productCoupons=cereBuyerCouponService.findCouponByProduct(sku.getTotal(),
                                    user.getBuyerUserId(),sku.getProductId());
                            //拼接活动id、满减金额字符串以便去重
                            if(!EmptyUtils.isEmpty(productCoupons)){
                                productCoupons.forEach(coupon -> {
                                    coupon.setDistinct(coupon.getActivityId()+"-"+coupon.getFullMoney());
                                    coupons.add(coupon);
                                });
                            }
                            if(!EmptyUtils.isEmpty(coupons)){
                                //去除重复活动和对应金额优惠券
                                List<ProductCoupon> collect= coupons.stream().collect(
                                        Collectors.collectingAndThen(Collectors.toCollection(
                                                () -> new TreeSet<>(Comparator.comparing(ProductCoupon::getDistinct))), ArrayList::new)
                                );
                                if(!EmptyUtils.isEmpty(collect)){
                                    //设置优惠券内容
                                    collect.forEach(tool -> {
                                        if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(tool.getCouponType())) {
                                            tool.setContent("满"+tool.getFullMoney().stripTrailingZeros().toPlainString()+
                                                    "减"+tool.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                                        } else {
                                            tool.setContent(tool.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                                        }
                                    });
                                }
                                settlement.setCoupons(collect);
                            }
                            List<ProductCoupon> shopCoupons=cereBuyerShopCouponService.findCouponByProduct(sku.getTotal(),
                                    user.getBuyerUserId(),sku.getProductId());
                            if(!EmptyUtils.isEmpty(shopCoupons)){
                                shopCoupons.forEach(coupon -> {
                                    coupon.setIds(cereBuyerShopCouponService.findProductIds(coupon.getShopCouponId()));
                                    if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(coupon.getCouponType())){
                                        coupon.setContent("满"+coupon.getFullMoney().stripTrailingZeros().toPlainString()+
                                                "减"+coupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                                    }else {
                                        coupon.setContent(coupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                                    }
                                });
                            }
                            settlementShop.setShopCoupons(shopCoupons);
                        }
                    }
                    settlementShop.setNumber(skus.stream().mapToInt(CartSku::getNumber).sum());
                    settlementShop.setSkus(skus);
                    settlementShop.setTotal(total);
                    shops.add(settlementShop);
                }
                settlement.setShops(shops);
            }
        }
        return settlement;
    }

    @Override
    public CereShopGroupWork findById(Long shopGroupWorkId) {
        return cereShopGroupWorkDAO.selectByPrimaryKey(shopGroupWorkId);
    }

    @Override
    public Long findByProductId(Long skuId) {
        return cereShopGroupWorkDAO.findByProductId(skuId);
    }

    @Override
    public GroupInvite getInvite(CollageIdParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        //根据订单id查询商品数据
        GroupInvite groupInvite=cereShopGroupWorkDAO.findProductByOrderId(param.getOrderId());
        if(groupInvite!=null){
            groupInvite.setCollageId(param.getCollageId());
            //查询拼团活动数据
            CereShopGroupWork cereShopGroupWork=cereShopGroupWorkDAO.findByCollageId(param.getCollageId());
            if(cereShopGroupWork!=null){
                //设置活动状态
                groupInvite.setActivityState(cereShopGroupWork.getState());
                //设置成团人数
                groupInvite.setPerson(cereShopGroupWork.getPerson());
                //查询开团数据
                CereCollageOrder cereCollageOrder=cereCollageOrderService.findById(param.getCollageId());
                if(cereCollageOrder!=null){
                    //设置拼单状态
                    groupInvite.setCollageOrderState(cereCollageOrder.getState());
                    //计算开团时间之后成团有效时长之后的时间
                    String endTime=TimeUtils.getMoreHourAfter(cereCollageOrder.getCreateTime(),cereShopGroupWork.getEffectiveTime());
                    //计算活动结束倒计时
                    groupInvite.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),endTime));
                    //查询参与拼团人员数据
                    List<CollagePerson> list=cereCollageOrderDetailService.findCollagePerson(param.getCollageId());
                    groupInvite.setPersonList(list);
                }
            }
        }
        return groupInvite;
    }

    @Override
    public ShareQrcode getShare(ShareQrcodeParam qrcodeParam) throws Exception {
        ShareQrcode shareQrcode=new ShareQrcode();

        String code_url = domain + "/h5/#/pages_category_page1/goodsModule/inviteSpell?collageId=" + qrcodeParam.getCollageId()
                + "&orderId=" + qrcodeParam.getOrderId()
                + "&productId=" + qrcodeParam.getProductId()
                + "&skuId=" + qrcodeParam.getSkuId()
                + "&shopGroupWorkId=" + qrcodeParam.getShopGroupWorkId();

        String scene = qrcodeParam.getCollageId()
                + "-" + qrcodeParam.getOrderId()
                + "-" + qrcodeParam.getProductId()
                + "-" + qrcodeParam.getSkuId()
                + "-" + qrcodeParam.getShopGroupWorkId();
        String page = "pages_category_page1/goodsModule/inviteSpell";
        WxMaCodeLineColor lineColor = new WxMaCodeLineColor("0", "0", "0");

        try {
            byte[] codeBytes = wxMaQrcodeService.createWxaCodeUnlimitBytes(scene, page, true, "release", 430, false, lineColor, true);
            String xcxQrCode = uploadService.uploadByte("applet_share.png", codeBytes);
            shareQrcode.setXcxQrcode(xcxQrCode);
        } catch (Exception e) {
            log.error("createWxaCodeUnlimitBytes fail: " + e.getMessage(), e);
        }

        //生成二维码
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 编码类型
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, 400, 400, hints);
        MatrixToImageConfig config = new MatrixToImageConfig();
        BufferedImage image = toBufferedImage(bitMatrix, config);

        //上传图片到OSS
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] qrcodeBytes = out.toByteArray();
        String qrcode = uploadService.uploadByte("h5_share.png", qrcodeBytes);
        shareQrcode.setQrcode(qrcode);
        return shareQrcode;
    }

    @Override
    public List<ShopGroupWorkUDetail> getGroupWorks(RenovationParam param) {
        List<ShopGroupWorkUDetail> list=cereShopGroupWorkDAO.getGroupWorks(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(detail -> {
                //查询商品明细
                detail.setProducts(cereShopGroupWorkDetailService.findDistinctProducts(detail.getShopGroupWorkId()));
            });
        }
        return list;
    }

    @Override
    public ProductDetail setActivityInfo(Long shopGroupWorkId, CereBuyerUser user, ProductParam param, ProductDetail detail) throws Exception {
        CereShopGroupWork cereShopGroupWork = null;
        Long activitySkuId = param.getSkuId();
        if (shopGroupWorkId != null && shopGroupWorkId > 0) {
            cereShopGroupWork = cereShopGroupWorkDAO.selectByPrimaryKey(shopGroupWorkId);
        } else {
            cereShopGroupWork = cereShopGroupWorkDAO.selectByProductId(detail.getProductId());
            activitySkuId = cereShopGroupWorkDetailService.findSkuIdByProductId(detail.getProductId());
        }

        if(cereShopGroupWork!=null){
            Long buyerUserId = null;
            if (user != null) {
                buyerUserId = user.getBuyerUserId();
            }
            shopGroupWorkId = cereShopGroupWork.getShopGroupWorkId();
            detail.setShopGroupWorkId(shopGroupWorkId);
            detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_GROUP.getCode());
            detail.setIfAdd(cereShopGroupWork.getIfAdd());
            detail.setStartTime(cereShopGroupWork.getStartTime());
            detail.setEndTime(cereShopGroupWork.getEndTime());
            if(IntegerEnum.ENABLE_START.getCode().equals(cereShopGroupWork.getIfEnable())
                    &&!EmptyUtils.isEmpty(cereShopGroupWork.getEnableTime())){
                //如果开启活动预热,计算预热几小时前的时间
                String enableTime=TimeUtils.headDate(cereShopGroupWork.getStartTime(),cereShopGroupWork.getEnableTime());
                //判断当前时间是否处于预热时间和活动开始时间之间
                if(TimeUtils.isBelong(enableTime,cereShopGroupWork.getStartTime())){
                    //如果处于,需要展示预热信息
                    detail.setIfEnable(IntegerEnum.YES.getCode());
                }
            }
            if(IntegerEnum.COUPON_STATE_START.getCode().equals(cereShopGroupWork.getState())){
                //活动进行中
                //设置活动倒计时
                detail.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereShopGroupWork.getEndTime()));
                detail.setIfEnable(IntegerEnum.NO.getCode());
            }

            //设置成团人数
            detail.setPerson(cereShopGroupWork.getPerson());

            //查询拼单数据
            List<CollageOrder> collageOrders=cereCollageOrderService.findCollageOrders(shopGroupWorkId,buyerUserId,param.getProductId());
            if(!EmptyUtils.isEmpty(collageOrders)){
                for (CollageOrder collage : collageOrders) {
                    //查询该拼单已拼人数
                    int people=cereCollageOrderService.findSpelled(collage.getCollageId());
                    //设置剩余成团人数
                    collage.setPerson(cereShopGroupWork.getPerson()-people);
                    //计算成团有效时间=拼单的发起时间延时拼团活动设置的有效时长后的时间
                    String time = TimeUtils.getMoreHourAfter(collage.getCreateTime(),cereShopGroupWork.getEffectiveTime());
                    //设置成团倒计时时间
                    collage.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),time));
                }
                collageOrders.removeIf(o -> o.getTime() <= 0);
                detail.setCollageOrders(collageOrders);
            }

            //设置库存总数 剩余库存数 原始价格 当前价格
            ProductStockInfo stockInfo = cereProductSkuService.findProductStockInfo(detail.getProductId());
            if (stockInfo != null) {
                detail.setTotal(stockInfo.getTotal());
                detail.setSurplusNumber(stockInfo.getStockNumber());
            }
            //detail.setOriginalPrice(detail.getPrice());
            detail.setSalePrice(detail.getPrice());
            BigDecimal price = cereShopGroupWorkDetailService.findPriceByGroupWorkIdAndSkuId(shopGroupWorkId, activitySkuId);
            detail.setPrice(price);

            //查询该商品所有组合规格数据封装到map
            List<Sku> skus=cereProductSkuService.findGroupWorkSkuByProductId(param.getProductId(),cereShopGroupWork.getShopGroupWorkId());
            if(!EmptyUtils.isEmpty(skus)) {
                Map<String, Sku> map = new HashMap<>();
                String image=detail.getImage();
                for (Sku sku:skus) {
                    if (sku.getActivityType() != null && sku.getActivityType() != 0) {
                        sku.setShopGroupWorkId(shopGroupWorkId);
                        sku.setStartTime(detail.getStartTime());
                        sku.setEndTime(detail.getEndTime());
                        sku.setTime(detail.getTime());
                        sku.setIfEnable(detail.getIfEnable());
                        sku.setPerson(detail.getPerson());
                        sku.setCollageOrders(detail.getCollageOrders());
                    }
                    if (EmptyUtils.isEmpty(sku.getImage())) {
                        sku.setImage(image);
                    }
                    if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                        sku.setValueCodes("单款项");
                    }
                    map.put(sku.getValueCodes(), sku);
                }
                detail.setMap(map);
            }
        }
        return detail;
    }

    @Override
    public List<BroadCastDTO> findCollageList(Long buyerUserId, Long shopGroupWorkId, String oneHourAgo) {
        return null;
    }

    @Override
    public ProductDetail setActivityInfoForRealInfo(Long shopGroupWorkId, CereBuyerUser user, ProductParam param, ProductDetail detail) throws Exception{
        CereShopGroupWork cereShopGroupWork = null;
        Long activitySkuId = param.getSkuId();
        if (shopGroupWorkId != null && shopGroupWorkId > 0) {
            cereShopGroupWork = cereShopGroupWorkDAO.selectByPrimaryKey(shopGroupWorkId);
        }
        if(cereShopGroupWork!=null){
            Long buyerUserId = null;
            if (user != null) {
                buyerUserId = user.getBuyerUserId();
            }
            //查询拼单数据
            List<CollageOrder> collageOrders = cereCollageOrderService.findCollageOrders(shopGroupWorkId, buyerUserId, param.getProductId());
            if(!EmptyUtils.isEmpty(collageOrders)){
                for (CollageOrder collage : collageOrders) {
                    //查询该拼单已拼人数
                    int people=cereCollageOrderService.findSpelled(collage.getCollageId());
                    //设置剩余成团人数
                    collage.setPerson(cereShopGroupWork.getPerson()-people);
                    //计算成团有效时间=拼单的发起时间延时拼团活动设置的有效时长后的时间
                    String time = TimeUtils.getMoreHourAfter(collage.getCreateTime(),cereShopGroupWork.getEffectiveTime());
                    //设置成团倒计时时间
                    collage.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),time));
                }
                collageOrders.removeIf(o -> o.getTime() <= 0);
                detail.setCollageOrders(collageOrders);
            }

            //设置库存总数 剩余库存数 原始价格 当前价格
            ProductStockInfo stockInfo = cereProductSkuService.findProductStockInfo(detail.getProductId());
            if (stockInfo != null) {
                detail.setTotal(stockInfo.getTotal());
                detail.setSurplusNumber(stockInfo.getStockNumber());
            }

            //查询该商品所有组合规格数据封装到map
            /*List<Sku> skus=cereProductSkuService.findGroupWorkSkuByProductId(param.getProductId(),cereShopGroupWork.getShopGroupWorkId());
            if(!EmptyUtils.isEmpty(skus)) {
                Map<String, Sku> map = new HashMap<>();
                String image=detail.getImage();
                for (Sku sku:skus) {
                    if (sku.getActivityType() != null && sku.getActivityType() != 0) {
                        sku.setShopGroupWorkId(shopGroupWorkId);
                        sku.setStartTime(detail.getStartTime());
                        sku.setEndTime(detail.getEndTime());
                        sku.setTime(detail.getTime());
                        sku.setIfEnable(detail.getIfEnable());
                        sku.setPerson(detail.getPerson());
                        sku.setCollageOrders(detail.getCollageOrders());
                    }
                    if (EmptyUtils.isEmpty(sku.getImage())) {
                        sku.setImage(image);
                    }
                    if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                        sku.setValueCodes("单款项");
                    }
                    map.put(sku.getValueCodes(), sku);
                }
                detail.setMap(map);
            }*/
        }
        return detail;
    }
}
