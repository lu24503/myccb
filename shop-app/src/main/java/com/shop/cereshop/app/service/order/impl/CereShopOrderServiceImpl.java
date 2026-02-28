/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.order.CereOrderDileverDAO;
import com.shop.cereshop.app.dao.order.CereOrderParentDAO;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.platformtool.CereBuyerPoliteRecordDAO;
import com.shop.cereshop.app.dao.product.CereProductStatsByDayDAO;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.dao.shop.CerePlatformShopDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.*;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.settlement.Distribution;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.param.comment.CommentSaveParam;
import com.shop.cereshop.app.param.compose.CereShopComposeDTO;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.param.discount.ShopPlatformDiscount;
import com.shop.cereshop.app.param.order.*;
import com.shop.cereshop.app.param.price.PriceCombineParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessDiscount;
import com.shop.cereshop.app.param.seckill.ShopBusinessSeckill;
import com.shop.cereshop.app.param.seckill.ShopPlatformSeckill;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.app.param.settlement.SettlementParam;
import com.shop.cereshop.app.param.settlement.ShopProductParam;
import com.shop.cereshop.app.pay.PayFactory;
import com.shop.cereshop.app.pay.PayService;
import com.shop.cereshop.app.pay.weixin.service.WxPayService;
import com.shop.cereshop.app.pay.weixin.skd.WXPayConstants;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.after.CereAfterDileverService;
import com.shop.cereshop.app.service.after.CereOrderAfterService;
import com.shop.cereshop.app.service.business.CereBusinessBuyerUserService;
import com.shop.cereshop.app.service.buyer.CereBuyerReceiveService;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.compose.CereShopComposeService;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.app.service.discount.CereShopDiscountDetailService;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.app.service.distributor.CereDistributionOrderService;
import com.shop.cereshop.app.service.distributor.CereDistributorBuyerService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.express.CerePlatformExpressService;
import com.shop.cereshop.app.service.express.KuaiDi100Service;
import com.shop.cereshop.app.service.express.KuaiDiNiaoService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderDetailService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkService;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.app.service.logistics.CereOrderLogisticsService;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.app.service.notice.CereNoticeService;
import com.shop.cereshop.app.service.order.*;
import com.shop.cereshop.app.service.order.placeOrderTemplate.PlaceOrderFactory;
import com.shop.cereshop.app.service.order.placeOrderTemplate.PlaceOrderTemplate;
import com.shop.cereshop.app.service.pay.CerePayLogService;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.app.service.platformtool.CerePlatformPoliteActivityService;
import com.shop.cereshop.app.service.platformtool.CerePlatformPoliteService;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.app.service.price.CerePriceProductService;
import com.shop.cereshop.app.service.price.CereShopPriceService;
import com.shop.cereshop.app.service.product.CereCommentWordService;
import com.shop.cereshop.app.service.product.CereProductMemberService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberService;
import com.shop.cereshop.app.service.scene.CereShopSceneService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillDetailService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.app.service.sensitive.CerePlatformSensitiveService;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
import com.shop.cereshop.app.service.shop.CereShopConversionService;
import com.shop.cereshop.app.service.stock.CereStockService;
import com.shop.cereshop.app.utils.DistributionUtil;
import com.shop.cereshop.commons.config.AlipayConfig;
import com.shop.cereshop.commons.constant.*;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.after.CereAfterDilever;
import com.shop.cereshop.commons.domain.after.CereOrderAfter;
import com.shop.cereshop.commons.domain.after.CereOrderDilever;
import com.shop.cereshop.commons.domain.business.CereBusinessBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.domain.distributor.CereDistributionOrder;
import com.shop.cereshop.commons.domain.express.ShippingTrace;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.notice.CereNotice;
import com.shop.cereshop.commons.domain.order.CereOrderParent;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.domain.order.CereOrderReconciliation;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.domain.pay.CerePayLog;
import com.shop.cereshop.commons.domain.platformtool.*;
import com.shop.cereshop.commons.domain.product.CereCommentWord;
import com.shop.cereshop.commons.domain.product.CereProductMember;
import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.shop.cereshop.commons.domain.product.CereProductStatsByDay;
import com.shop.cereshop.commons.domain.scene.CereShopScene;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import com.shop.cereshop.commons.domain.sensitive.CerePlatformSensitive;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.shop.cereshop.commons.domain.shop.CereShopComment;
import com.shop.cereshop.commons.domain.shop.CereShopConversion;
import com.shop.cereshop.commons.domain.tool.*;
import com.shop.cereshop.commons.domain.word.CerePlatformWord;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.*;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

@Slf4j
@Service
public class CereShopOrderServiceImpl implements CereShopOrderService {

    @Autowired
    private CereShopOrderDAO cereShopOrderDAO;

    @Autowired
    private CereOrderParentDAO cereOrderParentDAO;

    @Autowired
    private CereBuyerPoliteRecordDAO cereBuyerPoliteRecordDAO;

    @Autowired
    private CereOrderLogisticsService cereOrderLogisticsService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CereOrderParentService cereOrderParentService;

    @Autowired
    private CereDistributorBuyerService cereDistributorBuyerService;

    @Autowired
    private CereShopDistributorService cereShopDistributorService;

    @Autowired
    private CereOrderProductService cereOrderProductService;

    @Autowired
    private CereOrderProductAttributeService cereOrderProductAttributeService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private CerePlatformLogService cerePlatformLogService;

    @Autowired
    private CereDistributionOrderService cereDistributionOrderService;

    @Autowired
    private CereOrderReconciliationService cereOrderReconciliationService;

    @Autowired
    private CerePayLogService cerePayLogService;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereRedisKeyServcice cereRedisKeyServcice;

    @Autowired
    private CereShopCommentService cereShopCommentService;

    @Autowired
    private CerePlatformSensitiveService cerePlatformSensitiveService;

    @Autowired
    private CereShopConversionService cereShopConversionService;

    @Autowired
    private CereCommentWordService cereCommentWordService;

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

    @Autowired
    private CereShopCartService cereShopCartService;

    @Autowired
    private CereAfterDileverService cereAfterDileverService;

    @Autowired
    private CereBuyerReceiveService cereBuyerReceiveService;

    @Autowired
    private CerePlatformExpressService cerePlatformExpressService;

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    @Autowired
    private KuaiDi100Service kuaiDi100Service;

    @Autowired
    private KuaiDiNiaoService kuaiDiNiaoService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private CereOrderAfterService cereOrderAfterService;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Autowired
    private CereShopGroupWorkService cereShopGroupWorkService;

    @Autowired
    private CereCollageOrderService cereCollageOrderService;

    @Autowired
    private CereCollageOrderDetailService cereCollageOrderDetailService;

    @Autowired
    private CereShopSeckillService cereShopSeckillService;

    @Autowired
    private CereShopDiscountService cereShopDiscountService;

    @Autowired
    private CereShopSeckillDetailService cereShopSeckillDetailService;

    @Autowired
    private CereShopDiscountDetailService cereShopDiscountDetailService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CereNoticeService cereNoticeService;

    @Autowired
    private CereBusinessBuyerUserService cereBusinessBuyerUserService;

    @Autowired
    private CereShopPriceService cereShopPriceService;

    @Autowired
    private CerePriceProductService cerePriceProductService;

    @Autowired
    private CereShopComposeService cereShopComposeService;

    @Autowired
    private CerePlatformPoliteService cerePlatformPoliteService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CerePlatformMemberLevelService cerePlatformMemberLevelService;

    @Autowired
    private CerePlatformPoliteActivityService cerePlatformPoliteActivityService;

    @Autowired
    private CerePlatformSeckillService cerePlatformSeckillService;

    @Autowired
    private CerePlatformDiscountService cerePlatformDiscountService;

    @Autowired
    private CereShopSceneService cereShopSceneService;

    @Autowired
    private CereShopSceneMemberService cereShopSceneMemberService;

    @Autowired
    private CereProductMemberService cereProductMemberService;

    @Autowired
    private CereStockService cereStockService;

    @Autowired
    private PlaceOrderFactory placeOrderFactory;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private WxCardUtil wxCardUtil;

    @Autowired
    private CereProductStatsByDayDAO cereProductStatsByDayDAO;

    @Autowired
    private CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO;

    @Autowired
    private CereOrderDileverDAO cereOrderDileverDAO;

    @Autowired
    private CerePlatformShopDAO cerePlatformShopDAO;

    /**
     * app支付版本号
     */
    @Value("${weixin.app_pay_version}")
    private String app_pay_version;

    @Value("${querySkuRealInfo}")
    private boolean querySkuRealInfo;

    @Override
    public Settlement getSettlement(SettlementParam param, CereBuyerUser user) throws CoBusinessException {
        Settlement settlement = new Settlement();
        if (EmptyUtils.isEmpty(param.getReceiveId())) {
            //查询默认地址
            settlement.setReceive(cereBuyerReceiveService.findlatelyReceiveByUserId(user.getBuyerUserId()));
        } else {
            settlement.setReceive(cereBuyerReceiveService.findById(param.getReceiveId()));
        }
        List<ProductCoupon> coupons = new ArrayList<>();
        //结算处理
        if (querySkuRealInfo) {
            getSettlementInnerForRealInfo(settlement, param, user, coupons);
        } else {
            getSettlementInner(settlement, param, user, coupons);
        }

        List<ProductCoupon> couponList = null;
        if (!EmptyUtils.isEmpty(coupons)) {
            //去除重复活动和对应金额优惠券
            couponList = coupons.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(ProductCoupon::getDistinct))), ArrayList::new)
            );
            settlement.setCoupons(couponList);
        }
        settlement.setHuabeiChargeType(AlipayConfig.HUABEI_CHARGE_TYPE);
        settlement.setHuabeiFeerateList(AlipayConfig.HUABEI_FEERATE_LIST);
        return settlement;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public PayUrl submit(OrderParam param, CereBuyerUser user, String ip) throws CoBusinessException, Exception {
        String time = TimeUtils.yyMMddHHmmss();
        if (param.getPaymentMode() == null) {
            param.setPaymentMode(1);
        }
        //额外的校验
        PlaceOrderTemplate placeOrderTemplate = placeOrderFactory.getPlaceOrderTemplate(param.getType());
        return placeOrderTemplate.placeOrder(param, user, ip, time);
    }

    /**
     * 微信用户支付成功
     * 设置Redis拼团订单
     *
     * @param param PayParam拼单id和订单id必传
     * @param user  用户信息
     * @param ip    用户ip
     */
    @Override
    public void paySuccess(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException {
        if (param.getPaymentMode() == null) {
            param.setPaymentMode(1);
        }
        // 修改redis拼团数据
        if (!EmptyUtils.isLongEmpty(param.getCollageId())) {
            //定义加锁key
            String key = "拼团订单" + param.getCollageId();
            //加锁
            RLock redissonLock = redissonClient.getLock(key);
            redissonLock.lock();
            //查询拼单剩余拼团人数
            int person = cereCollageOrderService.findSurplusNumber(param.getCollageId());
            //如果是拼团单支付,查询redis缓存中是否存在该拼单数据
            Object obj = stringRedisService.get(String.valueOf(param.getCollageId()));
            if (EmptyUtils.isEmpty(obj)) {
                //如果没有,说明是发起拼团单第一个支付,放行并将拼单数据放到缓存
                stringRedisService.set(String.valueOf(param.getCollageId()), person);
            } else {
                //如果有,说明是参与拼团支付
                int number = (int) stringRedisService.get(String.valueOf(param.getCollageId()));
                if (number <= 0) {
                    //如果剩余拼团人数小于等于0,拼单已成团,无法支付了
                    // todo 退款
                    throw new CoBusinessException(CoReturnFormat.COLLAGE_ALREADY_SUCCESS, redissonLock);
                } else {
                    //如果大于0,还未成团,修改缓存中剩余拼团人数
                    stringRedisService.set(String.valueOf(param.getCollageId()), number - 1);
                }
            }
            //解锁
            redissonLock.unlock();
        } else {
            throw new CoBusinessException("参数错误");
        }
    }

    @Override
    public Integer findPayUsers(Long productId, Long skuId) {
        return cereShopOrderDAO.findPayUsers(productId, skuId);
    }

    @Override
    public List<BroadCastDTO> findRecentOrder(String oneHourAgo, Long productId) {
        return cereShopOrderDAO.findRecentOrder(oneHourAgo, productId);
    }

    @Override
    public int findUserShopOrderCount(Long shopId, Long buyerUserId) {
        return cereShopOrderDAO.findUserShopOrderCount(shopId, buyerUserId);
    }

    @Override
    public BigDecimal findUserShopOrderConsumption(Long shopId, Long buyerUserId) {
        return cereShopOrderDAO.findUserShopOrderConsumption(shopId, buyerUserId);
    }


    @Override
    public OrderPay gotoPay(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException, Exception {
//        if(EmptyUtils.isEmpty(user.getWechatOpenId())){
//            throw new CoBusinessException(CoReturnFormat.OPENID_IS_NULL);
//        }
        if (param.getPaymentMode() == null) {
            param.setPaymentMode(1);
        }

        // 判断redis是否存在拼团数据
        if (!EmptyUtils.isLongEmpty(param.getCollageId())) {
            //定义加锁key
            String key = "拼团订单" + param.getCollageId();
            //加锁
            RLock redissonLock = redissonClient.getLock(key);
            redissonLock.lock();
            Object obj = stringRedisService.get(String.valueOf(param.getCollageId()));
            if (!EmptyUtils.isEmpty(obj)) {
                int number = (int) stringRedisService.get(String.valueOf(param.getCollageId()));
                if (number <= 0) {
                    //如果剩余拼团人数小于等于0,拼单已成团,无法支付了
                    throw new CoBusinessException(CoReturnFormat.COLLAGE_ALREADY_SUCCESS, redissonLock);
                }
            }
            //解锁
            redissonLock.unlock();
        }
        List<Long> shopIds = null;
        String orderFormId = "";
        BigDecimal price = null;
        if (ParamEnum.ORDER_TYPE_PARENT.getCode().equals(param.getType())) {
            //父订单提交,查询父订单编号
            CereOrderParent parent = cereOrderParentService.findById(param.getOrderId());
            orderFormId = parent.getParentFormid();
            price = parent.getPrice();
            //根据父订单查询该订单下所有的店铺id
            shopIds = cereOrderParentService.findShopIds(param.getOrderId());
        } else {
            //子订单提交,查询子订单编号
            CereShopOrder order = cereShopOrderDAO.findById(param.getOrderId());
            orderFormId = order.getOrderFormid();
            price = order.getPrice();
            //根据子订单查询该订单店铺id
            shopIds = cereShopOrderDAO.findShopId(param.getOrderId());
        }
        System.out.println("支付订单号为：" + orderFormId);
        //调用支付接口
        PayService payService = PayFactory.getPayService(param.getPaymentMode());
        String openId = user.getWechatOpenId();
        if (Arrays.asList(IntegerEnum.ORDER_PAY_ALI.getCode(),
                IntegerEnum.ORDER_PAY_HUABEI.getCode()).contains(param.getPaymentMode())) {
            openId = user.getAliUserId();
        }
        Map<String, String> map = payService.gotoPay(orderFormId, price, openId, ip, ParamEnum.PAY_XCX.getCode(), param.getHuabeiPeriod());
        System.out.println("支付返回结果集" + map.toString());
        ObjectMapper mapper = new ObjectMapper();
        OrderPay pay = mapper.readValue(mapper.writeValueAsString(map), OrderPay.class);
        if (!EmptyUtils.isEmpty(shopIds)) {
            List<CereShopConversion> conversions = new ArrayList<>();
            shopIds.forEach(id -> {
                //新增转化数据
                CereShopConversion cereShopConversion = new CereShopConversion();
                cereShopConversion.setShopId(id);
                cereShopConversion.setCreateTime(TimeUtils.yyMMddHHmmss());
                cereShopConversion.setType(ParamEnum.CONVERSION_PAY.getCode());
                conversions.add(cereShopConversion);
            });
            //批量插入转化数据
            cereShopConversionService.insertBatch(conversions);
        }
        return pay;
    }


    @Override
    public OrderPay gotoAppPay(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException, Exception {
        // 判断redis是否存在拼团数据
        if (!EmptyUtils.isLongEmpty(param.getCollageId())) {
            //定义加锁key
            String key = "拼团订单" + param.getCollageId();
            //加锁
            RLock redissonLock = redissonClient.getLock(key);
            redissonLock.lock();
            Object obj = stringRedisService.get(String.valueOf(param.getCollageId()));
            if (!EmptyUtils.isEmpty(obj)) {
                int number = (int) stringRedisService.get(String.valueOf(param.getCollageId()));
                if (number <= 0) {
                    //如果剩余拼团人数小于等于0,拼单已成团,无法支付了
                    throw new CoBusinessException(CoReturnFormat.COLLAGE_ALREADY_SUCCESS, redissonLock);
                }
            }
            //解锁
            redissonLock.unlock();
        }
        List<Long> shopIds = null;
        String orderFormId = "";
        BigDecimal price = null;
        if (ParamEnum.ORDER_TYPE_PARENT.getCode().equals(param.getType())) {
            //父订单提交,查询父订单编号
            CereOrderParent parent = cereOrderParentService.findById(param.getOrderId());
            orderFormId = parent.getParentFormid();
            price = parent.getPrice();
            //根据父订单查询该订单下所有的店铺id
            shopIds = cereOrderParentService.findShopIds(param.getOrderId());
        } else {
            //子订单提交,查询子订单编号
            CereShopOrder order = cereShopOrderDAO.findById(param.getOrderId());
            orderFormId = order.getOrderFormid();
            price = order.getPrice();
            //根据子订单查询该订单店铺id
            shopIds = cereShopOrderDAO.findShopId(param.getOrderId());
        }
        //调用微信支付接口
        Integer payType = ParamEnum.PAY_APP.getCode();
        if (WXPayConstants.APP_PAY_VERSION_V3.equals(app_pay_version)) {
            payType = ParamEnum.PAY_APP_V3.getCode();
        }
        Map<String, String> map = wxPayService.gotoPay(orderFormId, price, user.getWechatOpenId(), ip, payType, param.getHuabeiPeriod());
        ObjectMapper mapper = new ObjectMapper();
        OrderPay pay = mapper.readValue(mapper.writeValueAsString(map), OrderPay.class);
        if (!EmptyUtils.isEmpty(shopIds)) {
            List<CereShopConversion> conversions = new ArrayList<>();
            shopIds.forEach(id -> {
                //新增转化数据
                CereShopConversion cereShopConversion = new CereShopConversion();
                cereShopConversion.setShopId(id);
                cereShopConversion.setCreateTime(TimeUtils.yyMMddHHmmss());
                cereShopConversion.setType(ParamEnum.CONVERSION_PAY.getCode());
                conversions.add(cereShopConversion);
            });
            //批量插入转化数据
            cereShopConversionService.insertBatch(conversions);
        }
        return pay;
    }

    @Override
    public OrderPay gotoH5Pay(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException, Exception {
        List<Long> shopIds = null;
        String orderFormId = "";
        BigDecimal price = null;
        if (ParamEnum.ORDER_TYPE_PARENT.getCode().equals(param.getType())) {
            //父订单提交,查询父订单编号
            CereOrderParent parent = cereOrderParentService.findById(param.getOrderId());
            orderFormId = parent.getParentFormid();
            price = parent.getPrice();
            //根据父订单查询该订单下所有的店铺id
            shopIds = cereOrderParentService.findShopIds(param.getOrderId());
        } else {
            //子订单提交,查询子订单编号
            CereShopOrder order = cereShopOrderDAO.findById(param.getOrderId());
            orderFormId = order.getOrderFormid();
            price = order.getPrice();
            //根据子订单查询该订单店铺id
            shopIds = cereShopOrderDAO.findShopId(param.getOrderId());
        }
        //调用微信支付接口
        Map<String, String> map = wxPayService.gotoPay(orderFormId, price, user.getWechatOpenId(), ip, ParamEnum.PAY_H5.getCode(), param.getHuabeiPeriod());
        ObjectMapper mapper = new ObjectMapper();
        OrderPay pay = mapper.readValue(mapper.writeValueAsString(map), OrderPay.class);
        if (!EmptyUtils.isEmpty(shopIds)) {
            List<CereShopConversion> conversions = new ArrayList<>();
            shopIds.forEach(id -> {
                //新增转化数据
                CereShopConversion cereShopConversion = new CereShopConversion();
                cereShopConversion.setShopId(id);
                cereShopConversion.setCreateTime(TimeUtils.yyMMddHHmmss());
                cereShopConversion.setType(ParamEnum.CONVERSION_PAY.getCode());
                conversions.add(cereShopConversion);
            });
            //批量插入转化数据
            cereShopConversionService.insertBatch(conversions);
        }
        return pay;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void handleWxLog(String orderFormId, String transactionId, String orderNo) throws Exception {
        String time = TimeUtils.yyMMddHHmmss();
        List<CereShopOrder> list = null;
        //查询是否为父订单编号
        CereOrderParent parent = cereShopOrderDAO.findByParentFormid(orderFormId);
        if (parent != null) {
            //查询所有子订单数据
            list = cereShopOrderDAO.findByParentId(parent.getParentId());
        } else {
            //子订单支付回调
            list = cereShopOrderDAO.findByFormid(orderFormId);
        }
        //支付业务处理
        if (!EmptyUtils.isEmpty(list)) {
            List<CereDistributionOrder> distributionOrders = new ArrayList<>();
            List<CereShopConversion> conversions = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            Long platformCouponId = null;
            List<Long> orderIdList = new ArrayList<>();
            for (CereShopOrder order : list) {
                total = total.add(order.getPrice());
                if (!EmptyUtils.isEmpty(order.getShopGroupWorkId())) {
                    //拼单支付业务处理
                    handleGroupWork(order, transactionId, orderNo, time);
                } else if (!EmptyUtils.isEmpty(order.getShopSeckillId())) {
                    //秒杀活动支付业务处理
                    handleSeckill(order, transactionId, orderNo, time);
                } else if (!EmptyUtils.isEmpty(order.getShopDiscountId())) {
                    //限时折扣活动支付业务处理
                    handleDiscount(order, transactionId, orderNo, time);
                } else {
                    //正常下单业务处理
                    handleOrder(order, transactionId, orderNo, time, distributionOrders, conversions);
                }
                CereBusinessBuyerUser bbu = new CereBusinessBuyerUser();
                bbu.setShopId(order.getShopId());
                bbu.setBuyerUserId(order.getBuyerUserId());
                bbu.setUpdateTime(time);
                cereBusinessBuyerUserService.refreshUpdateTime(bbu);
                if (order.getCouponId() != null) {
                    platformCouponId = order.getCouponId();
                }
                orderIdList.add(order.getOrderId());
            }
            //更新商品销量
            List<CartSku> statsList = cereOrderProductService.findProductStatsByOrderIds(orderIdList);
            if (CollectionUtils.isNotEmpty(statsList)) {
                Map<Long, List<CartSku>> productOpMap = statsList.stream().collect(Collectors.groupingBy(CartSku::getProductId));
                //更新日度统计
                for (Long productId : productOpMap.keySet()) {
                    List<CartSku> skuList = productOpMap.get(productId);
                    Long shopId = skuList.get(0).getShopId();
                    int number = skuList.stream().map(CartSku::getNumber).reduce((x, y) -> x += y).orElse(0);
                    CereProductStatsByDay stats = new CereProductStatsByDay();
                    stats.setShopId(shopId);
                    stats.setCreateDate(TimeUtils.today());
                    stats.setProductId(productId);
                    stats.setAddCartCount(0);
                    stats.setVisitCount(0);
                    stats.setSalesVolume(number);
                    cereProductStatsByDayDAO.insertOrUpdate(stats);
                }
                //更新sku实时信息(销量)
                for (CartSku stat : statsList) {
                    cereSkuMemberRealInfoDAO.increSalesVolumeBy(stat.getSkuId(), stat.getNumber());
                }

            }

            if (platformCouponId != null) {
                CereBuyerCoupon cereBuyerCoupon = cereBuyerCouponService.findLatestUsedCoupon(platformCouponId, list.get(0).getBuyerUserId());
                //核销卡券
                if (cereBuyerCoupon != null) {
                    CerePlatformActivity activity = cerePlatformActivityService.findById(cereBuyerCoupon.getCouponId());
                    CereBuyerUser user = cereBuyerUserService.selectByBuyerUserId(list.get(0).getBuyerUserId());
                    if (activity.getSyncCard() != null
                            && IntegerEnum.YES.getCode().equals(activity.getSyncCard())
                            && org.apache.commons.lang3.StringUtils.isNotBlank(activity.getCardId())
                            && org.apache.commons.lang3.StringUtils.isNotBlank(cereBuyerCoupon.getCouponCode())) {
                        wxCardUtil.useMerchantCoupon(activity.getCardId(), user.getWechatOpenId(), cereBuyerCoupon.getCouponCode());
                    }
                }
            }
            //支付金额四舍五入
            total = total.setScale(0, BigDecimal.ROUND_UP);
            //增加成长值
            cereBuyerUserService.updateGrowth(list.get(0).getBuyerUserId(), total.intValue());
            //增加积分(默认每下单1元，增加1积分)
            int creditOrderRate = 1;
            CerePlatformDict creditOrderRateDict = cerePlatformDictService.getByName(DictConstants.CREDIT_ORDER_RATE);
            CerePlatformDict switchDict = cerePlatformDictService.getByName(DictConstants.CREDIT_SWITCH);
            if (switchDict != null && StringUtils.isNotBlank(switchDict.getDictDescribe())
                    && IntegerEnum.YES.getCode().equals(Integer.parseInt(switchDict.getDictDescribe()))) {
                if (creditOrderRateDict != null) {
                    try {
                        creditOrderRate = Integer.parseInt(creditOrderRateDict.getDictDescribe());
                    } catch (Exception e) {
                        log.warn("credit_order_rate config abnormal");
                    }
                }
                cereBuyerUserService.increaseCredit(list.get(0).getBuyerUserId(), creditOrderRate * total.intValue(), CreditOptTypeEnum.PLACE_ORDER);
            }

            if (!EmptyUtils.isEmpty(distributionOrders)) {
                //批量插入分销订单数据
                cereDistributionOrderService.insertBatch(distributionOrders);
            }
            if (!EmptyUtils.isEmpty(conversions)) {
                //批量插入转化数据
                cereShopConversionService.insertBatch(conversions);
            }
        }
    }

    private void handleSeckill(CereShopOrder order, String transactionId, String orderNo, String time) throws Exception {
        //查询秒杀活动数据
        CereShopSeckill cereShopSeckill = cereShopSeckillService.findById(order.getShopSeckillId());
        if (cereShopSeckill != null) {
            //修改订单支付状态和订单状态
            order.setUpdateTime(time);
            order.setPaymentState(IntegerEnum.ORDER_PAY_SUCCESS.getCode());
            order.setPaymentTime(time);
            order.setState(IntegerEnum.ORDER_STAY_DILEVERY.getCode());
            cereShopOrderDAO.updateByPrimaryKeySelective(order);
            //新增订单对账数据
            addReconciliation(order, time);
            //新增支付流水数据
            addPayLog(order, time, orderNo, transactionId);
            //删除自动关闭订单延时任务
            stringRedisService.delete(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
            //删除延时任务记录
            cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
            // 以下逻辑已经修改为下单减少库存
            /*if(IntegerEnum.YES.getCode().equals(cereShopSeckill.getIfNumber())){
                //如果限量,查询活动商品剩余件数
                CereShopSeckillDetail detail=cereShopSeckillDetailService.findSkuDetail(order.getShopSeckillId(),order.getOrderId());
                if(detail!=null){
                    //查询订单商品购买数量
                    int number=cereShopOrderDAO.findOrderNumber(order.getOrderId());
                    //扣除订单购买数量
                    detail.setNumber(detail.getNumber()-number);
                    cereShopSeckillDetailService.updateNumber(detail);
                }
            }*/
            //新增订单支付成功消息
            CereNotice cereNotice = new CereNotice();
            cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
            cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
            cereNotice.setBuyerUserId(order.getBuyerUserId());
            cereNotice.setShopId(order.getShopId());
            cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_PAY_SUCCESS.getCode());
            cereNotice.setNoticeContent("您购买的" + order.getOrderFormid() + "已支付成功，请耐心等待商家发货");
            cereNotice.setOnly(order.getOrderId());
            cereNotice.setCreateTime(time);
            cereNotice.setIfRead(IntegerEnum.NO.getCode());
            cereNoticeService.insert(cereNotice);
        }
    }

    private void handleDiscount(CereShopOrder order, String transactionId, String orderNo, String time) throws Exception {
        //查询限时折扣活动数据
        CereShopDiscount cereShopDiscount = cereShopDiscountService.findById(order.getShopDiscountId());
        if (cereShopDiscount != null) {
            //修改订单支付状态和订单状态
            order.setUpdateTime(time);
            order.setPaymentState(IntegerEnum.ORDER_PAY_SUCCESS.getCode());
            order.setPaymentTime(time);
            order.setState(IntegerEnum.ORDER_STAY_DILEVERY.getCode());
            cereShopOrderDAO.updateByPrimaryKeySelective(order);
            //新增订单对账数据
            addReconciliation(order, time);
            //新增支付流水数据
            addPayLog(order, time, orderNo, transactionId);
            //删除自动关闭订单延时任务
            stringRedisService.delete(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
            //删除延时任务记录
            cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
            //以下逻辑改为下单时减库存
            /*if(IntegerEnum.YES.getCode().equals(cereShopDiscount.getIfNumber())){
                //如果限量,查询活动商品剩余件数
                CereShopDiscountDetail detail=cereShopDiscountDetailService.findSkuDetail(order.getShopDiscountId(),order.getOrderId());
                if(detail!=null){
                    //查询订单商品购买数量
                    int number=cereShopOrderDAO.findOrderNumber(order.getOrderId());
                    //扣除订单购买数量
                    detail.setNumber(detail.getNumber()-number);
                    cereShopDiscountDetailService.updateNumber(detail);
                }
            }*/
            //新增订单支付成功消息
            CereNotice cereNotice = new CereNotice();
            cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
            cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
            cereNotice.setBuyerUserId(order.getBuyerUserId());
            cereNotice.setShopId(order.getShopId());
            cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_PAY_SUCCESS.getCode());
            cereNotice.setNoticeContent("您购买的" + order.getOrderFormid() + "已支付成功，请耐心等待商家发货");
            cereNotice.setOnly(order.getOrderId());
            cereNotice.setCreateTime(time);
            cereNotice.setIfRead(IntegerEnum.NO.getCode());
            cereNoticeService.insert(cereNotice);
        }
    }

    private void handleOrder(CereShopOrder order, String transactionId, String orderNo, String time,
                             List<CereDistributionOrder> distributionOrders, List<CereShopConversion> conversions) throws Exception {
        //修改订单支付状态和订单状态
        order.setUpdateTime(time);
        order.setPaymentState(IntegerEnum.ORDER_PAY_SUCCESS.getCode());
        order.setPaymentTime(time);
        order.setState(IntegerEnum.ORDER_STAY_DILEVERY.getCode());
        //校验当前客户是否为分销员
        ShopDistributor distributor = null;
        if (!EmptyUtils.isEmpty(order.getCustomerPhone())) {
            distributor = cereShopDistributorService.findByPhone(order.getCustomerPhone(), order.getShopId());
        }
        if (distributor != null) {
            if (IntegerEnum.YES.getCode().equals(distributor.getIfSelf())) {
                //当前客户就是分销员,并且开启了自购分佣,新增分销订单数据
                addDistributor(distributor, order, time, distributionOrders);
            }
        } else {
            //如果客户不是分销员,判断是否绑定其他分销员,且必须为shopId对应的分销员
            distributor = cereDistributorBuyerService.findByUserId(order.getBuyerUserId(), order.getShopId());
            if (distributor != null) {
                //如果绑定了,新增分销订单数据
                addDistributor(distributor, order, time, distributionOrders);
            }
        }
        cereShopOrderDAO.updateByPrimaryKeySelective(order);
        //新增订单对账数据
        addReconciliation(order, time);
        //新增支付流水数据
        addPayLog(order, time, orderNo, transactionId);
        //新增转化数据
        CereShopConversion cereShopConversion = new CereShopConversion();
        cereShopConversion.setType(ParamEnum.CONVERSION_PAY_SUCCESS.getCode());
        cereShopConversion.setShopId(order.getShopId());
        cereShopConversion.setCreateTime(time);
        conversions.add(cereShopConversion);
        //删除自动关闭订单延时任务
        stringRedisService.delete(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
        //删除延时任务记录
        cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
        //新增订单支付成功消息
        CereNotice cereNotice = new CereNotice();
        cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
        cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
        cereNotice.setBuyerUserId(order.getBuyerUserId());
        cereNotice.setShopId(order.getShopId());
        cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_PAY_SUCCESS.getCode());
        cereNotice.setNoticeContent("您购买的" + order.getOrderFormid() + "已支付成功，请耐心等待商家发货");
        cereNotice.setOnly(order.getOrderId());
        cereNotice.setCreateTime(time);
        cereNotice.setIfRead(IntegerEnum.NO.getCode());
        cereNoticeService.insert(cereNotice);
    }

    private void handleGroupWork(CereShopOrder order, String transactionId, String orderNo, String time) throws Exception {
        //根据订单id和拼团活动id查询拼单数据
        CereCollageOrder cereCollageOrder = cereCollageOrderService.findByOrder(order);
        if (cereCollageOrder != null) {
            //修改订单支付状态和订单状态
            order.setUpdateTime(time);
            order.setPaymentState(IntegerEnum.ORDER_PAY_SUCCESS.getCode());
            order.setPaymentTime(time);
            //新增订单对账数据
            addReconciliation(order, time);
            //新增支付流水数据
            addPayLog(order, time, orderNo, transactionId);
            if (cereCollageOrder.getSurplusNumber() > 1) {
                //待成团业务处理,拼单剩余拼团人数-1
                cereCollageOrder.setSurplusNumber(cereCollageOrder.getSurplusNumber() - 1);
                cereCollageOrder.setUpdateTime(time);
                cereCollageOrderService.update(cereCollageOrder);
                //修改订单状态为待成团
                order.setState(IntegerEnum.ORDER_STAY_COLLAGE.getCode());
            } else {
                //拼团成功业务处理,状态改为拼团成功
                cereCollageOrder.setUpdateTime(time);
                cereCollageOrder.setState(IntegerEnum.COLLAGE_STATE_SUCCESS.getCode());
                cereCollageOrder.setSurplusNumber(0);
                cereCollageOrderService.update(cereCollageOrder);
                //查询所有参与拼团的未支付的订单数据,当前订单除外,修改为失效订单
                List<Long> ids = cereCollageOrderService.findNotPayCollageOrderIds(cereCollageOrder.getCollageId(), order.getOrderId());
                if (!EmptyUtils.isEmpty(ids)) {
                    cereCollageOrderDetailService.updateInvalid(ids);
                    //取消所有的失效订单
                    cancelBatch(ids);
                }
                //查询所有参与拼团支付过的订单数据,当前订单除外,修改为待发货
                List<CereShopOrder> orders = cereCollageOrderService.findPayCollageOrderIds(cereCollageOrder.getCollageId(), order.getOrderId());
                if (!EmptyUtils.isEmpty(orders)) {
                    //批量修改订单状态
                    cereShopOrderDAO.updateBatch(orders, IntegerEnum.ORDER_STAY_DILEVERY.getCode(), time);
                    //封装支付成功消息
                    List<CereNotice> notices = orders.stream().map(cereShopOrder -> {
                        CereNotice cereNotice = new CereNotice();
                        cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
                        cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
                        cereNotice.setBuyerUserId(cereShopOrder.getBuyerUserId());
                        cereNotice.setShopId(cereShopOrder.getShopId());
                        cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_PAY_SUCCESS.getCode());
                        cereNotice.setNoticeContent("您购买的" + cereShopOrder.getOrderFormid() + "已支付成功，请耐心等待商家发货");
                        cereNotice.setOnly(cereShopOrder.getOrderId());
                        cereNotice.setIfRead(IntegerEnum.NO.getCode());
                        cereNotice.setCreateTime(time);
                        return cereNotice;
                    }).collect(Collectors.toList());
                    //批量插入消息
                    cereNoticeService.insertBatch(notices);
                }
                //删除拼团失败延时任务
                stringRedisService.delete(StringEnum.COLLAGE_ERROR.getCode() + "-" + cereCollageOrder.getCollageId());
                cereRedisKeyServcice.deleteByKey(StringEnum.COLLAGE_ERROR.getCode() + "-" + cereCollageOrder.getCollageId());
                //修改订单状态为待发货
                order.setState(IntegerEnum.ORDER_STAY_DILEVERY.getCode());
                //新增订单支付成功消息
                CereNotice cereNotice = new CereNotice();
                cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
                cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
                cereNotice.setBuyerUserId(order.getBuyerUserId());
                cereNotice.setShopId(order.getShopId());
                cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_PAY_SUCCESS.getCode());
                cereNotice.setNoticeContent("您购买的" + order.getOrderFormid() + "已支付成功，请耐心等待商家发货");
                cereNotice.setOnly(order.getOrderId());
                cereNotice.setCreateTime(time);
                cereNotice.setIfRead(IntegerEnum.NO.getCode());
                cereNoticeService.insert(cereNotice);
            }
            cereShopOrderDAO.updateByPrimaryKeySelective(order);
        }
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void handleRefundWxLog(String orderFormId, String transaction_id, String orderNo) throws Exception {
        String time = TimeUtils.yyMMddHHmmss();
        //查询订单数据
        CereShopOrder order = cereShopOrderDAO.findByOrderFormid(orderFormId);
        if (order != null) {
            //新增对账单数据
            CereOrderReconciliation reconciliation = new CereOrderReconciliation();
            reconciliation.setOrderId(order.getOrderId());
            reconciliation.setMoney(order.getPrice());
            reconciliation.setState(IntegerEnum.RELATIONSHIP_SOLVE_FROZEN.getCode());
            reconciliation.setType(IntegerEnum.RELATIONSHIP_ONCOME.getCode());
            reconciliation.setCreateTime(time);
            cereOrderReconciliationService.insert(reconciliation);
            //查询支付流水
            CerePayLog cerePayLog = cereShopOrderDAO.findPayLog(order.getOrderFormid(), StringEnum.PAY_LOG_PAY.getCode());
            //插入退款流水
            CerePayLog payLog = new CerePayLog();
            payLog.setCreateTime(time);
            payLog.setOrderFormid(order.getOrderFormid());
            payLog.setOutTradeNo(orderNo);
            payLog.setTransactionId(transaction_id);
            payLog.setPaymentMode(order.getPaymentMode());
            payLog.setShopId(order.getShopId());
            payLog.setState(StringEnum.PAY_LOG_REFUND.getCode());
            payLog.setTotalFee(order.getPrice());
            payLog.setUserId(String.valueOf(order.getBuyerUserId()));
            payLog.setOutRefundNo(cerePayLog.getOutRefundNo());
            payLog.setRemark(order.getCustomerName() + "取消订单退款" + order.getPrice() + "元,退款单号为：" + cerePayLog.getOutRefundNo()
                    + ",退款时间为" + time);
            cerePayLogService.insert(payLog);

            //更新订单状态为已取消
            order.setUpdateTime(time);
            order.setState(IntegerEnum.ORDER_STOP.getCode());
            order.setPrice(BigDecimal.ZERO);
            order.setPaymentState(IntegerEnum.NO.getCode());
            cereShopOrderDAO.updateByPrimaryKeySelective(order);

            //扣减成长值
            int growth = payLog.getTotalFee().setScale(0, BigDecimal.ROUND_UP).negate().intValue();
            log.info(" buyerUserId {} growth {}", order.getBuyerUserId(), growth);
            cereBuyerUserService.updateGrowth(order.getBuyerUserId(), growth);
        }
    }

    @Override
    public SettlementShop findSettlementShop(Long shopId) {
        return cereShopOrderDAO.findSettlementShop(shopId);
    }

    @Override
    public Page getAll(OrderGetAllParam param, CereBuyerUser user) throws CoBusinessException, Exception {
        param.setBuyerUserId(user.getBuyerUserId());
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Orders> list = cereShopOrderDAO.getAll(param);
        if (!EmptyUtils.isEmpty(list)) {
            List<Long> orderIdList = list.stream().map(Orders::getOrderId).collect(Collectors.toList());
            Map<Long, List<CereOrderDilever>> dileverMap = cereOrderDileverDAO
                    .selectList(Wrappers.<CereOrderDilever>lambdaQuery().in(CereOrderDilever::getOrderId, orderIdList))
                    .stream().collect(Collectors.groupingBy(CereOrderDilever::getOrderId));
            List<Long> dictIdList = dileverMap.values().stream().flatMap(Collection::stream).map(CereOrderDilever::getExpress).collect(Collectors.toList());
            Map<Long, String> dictNameMap = new HashMap<>();
            if (dictIdList.size() > 0) {
                dictNameMap = cerePlatformDictService.selectDictNameMap(dictIdList);
            }
            //查拼单数据
            Map<Long, Long> collageIdMap = cereCollageOrderDetailService.findByOrderIdList(orderIdList)
                    .stream().collect(Collectors.groupingBy(CereCollageOrderDetail::getOrderId,
                            Collectors.collectingAndThen(Collectors.toList(), lst -> lst.stream().map(CereCollageOrderDetail::getCollageId).findFirst().orElse(null))));
            //查售后数据
            List<CereOrderAfter> afterList = cereOrderAfterService.selectByOrderIdList(orderIdList);
            afterList.sort((o1, o2) -> {
                if (o1.getUpdateTime() == null) {
                    return -1;
                }
                if (o2.getUpdateTime() == null) {
                    return 1;
                }
                return o2.getUpdateTime().compareTo(o1.getUpdateTime());
            });
            Map<Long, Integer> afterStateMap = afterList.stream().collect(Collectors.groupingBy(CereOrderAfter::getOrderId,
                    Collectors.collectingAndThen(Collectors.toList(), lst -> lst.stream().map(CereOrderAfter::getAfterState).findFirst().orElse(null))));
            for (Orders orders : list) {
                Long orderId = orders.getOrderId();
                //设置物流公司名称
                List<CereOrderDilever> deliverList = dileverMap.get(orderId);
                if (deliverList != null && deliverList.size() > 0) {
                    String expressName = dictNameMap.get(deliverList.get(0).getExpress());
                    orders.setExpress(expressName);
                    orders.setDeliverFormid(deliverList.get(0).getDeliverFormid());
                }
                //设置拼单id
                orders.setCollageId(collageIdMap.get(orderId));
                //设置售后状态 一个订单可能有多个商品，分多次售后，这里查最近一次更新售后的状态
                Integer afterState = afterStateMap.get(orderId);
                orders.setAfterState(afterState != null ? afterState.toString() : null);
                //计算自动关闭订单倒计时
                orders.setTime(30 * 60 * 1000 - TimeUtils.getCountDownByTime(orders.getCreateTime(), TimeUtils.yyMMddHHmmss()));
                //查询订单商品明细数据
                List<CartSku> skus = cereOrderProductService.findOrderProductSku(orders.getOrderId());
                if (!EmptyUtils.isEmpty(skus)) {
                    //设置规格值数组
                    skus.forEach(sku -> sku.setValues(EmptyUtils.getImages(sku.getValue())));
                }
                //查询该订单已评价商品数量
                int comment = cereShopCommentService.findProductNumber(orders.getOrderId());
                if (skus.size() == comment) {
                    //该订单商品全部已评价
                    orders.setIfNotComment(IntegerEnum.NO.getCode());
                } else {
                    orders.setIfNotComment(IntegerEnum.YES.getCode());
                }
                orders.setSkus(skus);
            }
        }
        PageInfo<Orders> pageInfo = new PageInfo<>(list);
        Page page = new Page(pageInfo.getList(), pageInfo.getTotal());
        return page;
    }

    @Override
    public OrderDetail getById(OrderGetByIdParam param) throws CoBusinessException, Exception {
        Long orderId = param.getOrderId();
        /*if(!EmptyUtils.isLongEmpty(param.getNoticeId())){
            //更新已读状态
            CereNotice cereNotice=new CereNotice();
            cereNotice.setIfRead(IntegerEnum.YES.getCode());
            cereNotice.setNoticeId(param.getNoticeId());
            cereNoticeService.update(cereNotice);
        }*/
        OrderDetail detail = cereShopOrderDAO.getById(orderId);
        if (detail != null) {
            //查询订单商品明细
            List<CartSku> skus = cereOrderProductService.findOrderProductSku(detail.getOrderId());
            if (!EmptyUtils.isEmpty(skus)) {
                //设置规格值数组
                skus.forEach(sku -> sku.setValues(EmptyUtils.getImages(sku.getValue())));
            }
            detail.setSkus(skus);
            //计算自动关闭订单倒计时
            detail.setTime(30 * 60 * 1000 - TimeUtils.getCountDownByTime(detail.getCreateTime(), TimeUtils.yyMMddHHmmss()));
            if (!EmptyUtils.isEmpty(detail.getShopGroupWorkId())) {
                //查询拼团信息数据
                CollageDetail collageDetail = cereCollageOrderService.findDetail(detail.getShopGroupWorkId(), detail.getOrderId());
                if (collageDetail != null) {
                    //计算成团有效时间=拼单的发起时间延时拼团活动设置的有效时长后的时间
                    String time = TimeUtils.getMoreHourAfter(collageDetail.getCreateTime(), collageDetail.getEffectiveTime());
                    //设置成团倒计时时间
                    collageDetail.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(), time));
                    detail.setTime(collageDetail.getTime());
                    //查询参与拼团人员信息
                    collageDetail.setPersonList(cereCollageOrderService.findPersons(collageDetail.getCollageId()));
                    detail.setCollageDetail(collageDetail);
                }
            }
        }
        return detail;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void confirm(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        //校验当前订单状态是否为待收货
        CereShopOrder cereShopOrder = cereShopOrderDAO.checkState(param.getOrderId(), IntegerEnum.ORDER_HAVE_DILEVERY.getCode());
        if (cereShopOrder == null) {
            throw new CoBusinessException(CoReturnFormat.ORDER_NOT_TAKE);
        }
        //修改订单状态为已完成
        CereShopOrder order = cereShopOrderDAO.selectByPrimaryKey(param.getOrderId());
        order.setOrderId(param.getOrderId());
        order.setUpdateTime(time);
        order.setState(IntegerEnum.ORDER_FINISH.getCode());
        order.setReceiveTime(time);
        cereShopOrderDAO.updateByPrimaryKeySelective(order);
        //删除自动确认收货延时任务
        stringRedisService.delete(StringEnum.ORDER_CONFIRM_DILEVERY.getCode() + "-" + param.getOrderId());
        cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_CONFIRM_DILEVERY.getCode() + "-" + param.getOrderId());
        //修改订单对账单状态为解冻
        CereOrderReconciliation reconciliation = new CereOrderReconciliation();
        reconciliation.setOrderId(param.getOrderId());
        reconciliation.setState(IntegerEnum.RELATIONSHIP_SOLVE_FROZEN.getCode());
        reconciliation.setUpdateTime(time);
        cereOrderReconciliationService.updateByOrderId(reconciliation);
        //新增订单已完成消息
        CereNotice cereNotice = new CereNotice();
        cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
        cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
        cereNotice.setBuyerUserId(order.getBuyerUserId());
        cereNotice.setShopId(order.getShopId());
        cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_FINISH.getCode());
        cereNotice.setNoticeContent("订单" + order.getOrderFormid() + "已确认收货，期待您分享商品评价和购物心得");
        cereNotice.setOnly(order.getOrderId());
        cereNotice.setCreateTime(time);
        cereNotice.setIfRead(IntegerEnum.NO.getCode());
        cereNoticeService.insert(cereNotice);
    }

    @Override
    public void update(CereShopOrder cereShopOrder) throws CoBusinessException {
        cereShopOrderDAO.updateByPrimaryKeySelective(cereShopOrder);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void cancel(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException, Exception {
        String time = TimeUtils.yyMMddHHmmss();
        //校验当前订单状态是否为待付款或者待发货或者待成团
        CereShopOrder cereShopOrder = cereShopOrderDAO.checkCancelState(param.getOrderId());
        if (cereShopOrder == null) {
            throw new CoBusinessException(CoReturnFormat.ORDER_NOT_TAKE);
        }
        //修改订单状态为已取消
        CereShopOrder order = new CereShopOrder();
        order.setOrderId(param.getOrderId());
        order.setUpdateTime(time);
        order.setState(IntegerEnum.ORDER_STOP.getCode());
        cereShopOrderDAO.updateByPrimaryKeySelective(order);
        if (!EmptyUtils.isEmpty(cereShopOrder.getCouponId())) {
            //修改平台优惠券状态为已领取
            CereBuyerCoupon cereBuyerCoupon = new CereBuyerCoupon();
            cereBuyerCoupon.setBuyerUserId(user.getBuyerUserId());
            cereBuyerCoupon.setCouponId(cereShopOrder.getCouponId());
            cereBuyerCoupon.setUpdateTime(time);
            cereBuyerCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
            cereBuyerCouponService.updateByUserIdAndCouponId(cereBuyerCoupon);
        }
        if (!EmptyUtils.isEmpty(cereShopOrder.getId())) {
            //修改店铺优惠券状态为已领取
            CereBuyerShopCoupon cereBuyerShopCoupon = new CereBuyerShopCoupon();
            cereBuyerShopCoupon.setId(cereShopOrder.getId());
            cereBuyerShopCoupon.setUpdateTime(time);
            cereBuyerShopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
            cereBuyerShopCouponService.updateState(cereBuyerShopCoupon);
        }
        if (!EmptyUtils.isLongEmpty(cereShopOrder.getShopGroupWorkId())) {
            //查询当前订单是否为开团用户
            CereCollageOrder cereCollageOrder = cereCollageOrderService.findByUserId(user.getBuyerUserId(), order.getOrderId());
            if (cereCollageOrder != null) {
                //修改拼单状态为失败
                cereCollageOrder.setUpdateTime(time);
                cereCollageOrder.setState(IntegerEnum.COLLAGE_STATE_ERROR.getCode());
                cereCollageOrderService.update(cereCollageOrder);
                //查询参与该拼单的订单数据
                List<Long> ids = cereCollageOrderService.findOrderIds(cereCollageOrder.getCollageId());
                //修改拼单明细订单改为已失效,并且订单自动取消
                if (!EmptyUtils.isEmpty(ids)) {
                    cereCollageOrderDetailService.updateInvalid(ids);
                    //过滤当前订单id
                    ids = ids.stream().filter(id -> !id.equals(cereShopOrder.getOrderId())).collect(Collectors.toList());
                    if (!EmptyUtils.isEmpty(ids)) {
                        //批量取消订单
                        cancelBatch(ids);
                    }
                }
            }
        }

        //删除自动关闭订单延时任务
        stringRedisService.delete(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());
        //删除延时任务记录
        cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId());

        //订单库存回流
        cereStockService.rollbackStockByOrderId(order.getOrderId());
        //积分回流
        cereBuyerUserService.rollbackCreditByOrderId(order.getOrderId(), cereShopOrder.getBuyerUserId());

        //删除自动确认收货延时任务
        stringRedisService.delete(StringEnum.ORDER_CONFIRM_DILEVERY.getCode() + "-" + param.getOrderId());
        cereRedisKeyServcice.deleteByKey(StringEnum.ORDER_CONFIRM_DILEVERY.getCode() + "-" + param.getOrderId());
        if (IntegerEnum.ORDER_STAY_DILEVERY.getCode().equals(cereShopOrder.getState())
                || IntegerEnum.YES.getCode().equals(cereShopOrder.getPaymentState())) {
            //如果是待发货或者待成团（且付款）取消,需要退款,查询支付流水数据
            CerePayLog refund = cereShopOrderDAO.findPayLog(cereShopOrder.getOrderFormid(), StringEnum.PAY_LOG_PAY.getCode());
            if (refund != null) {
                //微信支付没通,暂时直接处理业务
//                handleRefundTestWxLog(order.getOrderFormid(),refund.getTransactionId(),refund.getOutTradeNo());
                /*Map<String, String> resultMap = wxPayService.orderRefund(refund.getTransactionId(), refund.getOutRefundNo(), refund.getTotalFee(), cereShopOrder.getPrice());
                if(!EmptyUtils.isEmpty(resultMap)){
                    if(resultMap.get("return_msg").equals(WxPayEnum.REFUND_SUCCESS.getCode())&&
                            resultMap.get("return_code").equals(WxPayEnum.REFUND_OK.getCode())){
                        //退款成功
                    }else if(resultMap.get("return_code").equals(WxPayEnum.BUSINESS_BALANCE_NOTENOUGH.getCode())){
                        //退款失败,商户余额不足
                        throw new CoBusinessException(CoReturnFormat.BUSINESS_BALANCE_NOT);
                    }
                }*/
                PayService payService = PayFactory.getPayService(refund.getPaymentMode());
                Map<String, String> resultMap = payService.orderRefund(refund.getTransactionId(), refund.getOutRefundNo(), refund.getTotalFee(), cereShopOrder.getPrice());
                if (!EmptyUtils.isEmpty(resultMap)) {
                    if (resultMap.get("return_msg").equals(WxPayEnum.REFUND_SUCCESS.getCode()) &&
                            resultMap.get("return_code").equals(WxPayEnum.REFUND_OK.getCode())) {
                        //退款成功 支付宝的退款是立即退款的，需要同步处理
                        if (Arrays.asList(IntegerEnum.ORDER_PAY_ALI.getCode(),
                                IntegerEnum.ORDER_PAY_HUABEI.getCode()).contains(cereShopOrder.getPaymentMode())) {
                            //内部调用不会执行事务，所以通过springUtil获取service对象
                            CereShopOrderService cereShopOrderService = SpringUtil.getBean(CereShopOrderService.class);
                            cereShopOrderService.handleRefundWxLog(refund.getOrderFormid(), refund.getTransactionId(), refund.getOutTradeNo());
                        }
                    } else if (resultMap.get("return_code").equals(WxPayEnum.BUSINESS_BALANCE_NOTENOUGH.getCode())) {
                        //退款失败,商户余额不足
                        throw new CoBusinessException(CoReturnFormat.BUSINESS_BALANCE_NOT);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void cancelBatch(List<Long> ids) throws CoBusinessException, Exception {
        String time = TimeUtils.yyMMddHHmmss();
        //查询订单信息
        List<CereShopOrder> orders = cereShopOrderDAO.findByIds(ids);
        for (CereShopOrder order : orders) {
            //修改订单状态为已取消
            order.setUpdateTime(time);
            order.setState(IntegerEnum.ORDER_STOP.getCode());
            cereShopOrderDAO.updateByPrimaryKeySelective(order);

            //订单库存回流
            cereStockService.rollbackStockByOrderId(order.getOrderId());

            //积分回流
            cereBuyerUserService.rollbackCreditByOrderId(order.getOrderId(), order.getBuyerUserId());

            if (IntegerEnum.YES.getCode().equals(order.getPaymentState())) {
                //如果付款了,需要退款,查询支付流水数据
                CerePayLog refund = cereShopOrderDAO.findPayLog(order.getOrderFormid(), StringEnum.PAY_LOG_PAY.getCode());
                if (refund != null) {
                    //微信支付没通,暂时直接处理业务
//                    handleRefundTestWxLog(order.getOrderFormid(),refund.getTransactionId(),refund.getOutTradeNo());
                    PayService payService = PayFactory.getPayService(refund.getPaymentMode());
                    Map<String, String> resultMap = payService.orderRefund(refund.getTransactionId(), refund.getOutRefundNo(), refund.getTotalFee(), refund.getTotalFee());
                    if (!EmptyUtils.isEmpty(resultMap)) {
                        if (resultMap.get("return_msg").equals(WxPayEnum.REFUND_SUCCESS.getCode()) &&
                                resultMap.get("return_code").equals(WxPayEnum.REFUND_OK.getCode())) {
                            //退款成功 支付宝的退款是立即退款的，需要同步处理
                            if (Arrays.asList(IntegerEnum.ORDER_PAY_ALI.getCode(),
                                    IntegerEnum.ORDER_PAY_HUABEI.getCode()).contains(order.getPaymentMode())) {
                                //内部调用不会执行事务，所以通过springUtil获取service对象
                                CereShopOrderService cereShopOrderService = SpringUtil.getBean(CereShopOrderService.class);
                                cereShopOrderService.handleRefundWxLog(refund.getOrderFormid(), refund.getTransactionId(), refund.getOutTradeNo());
                            }
                        } else if (resultMap.get("return_code").equals(WxPayEnum.BUSINESS_BALANCE_NOTENOUGH.getCode())) {
                            //退款失败,商户余额不足
                            throw new CoBusinessException(CoReturnFormat.BUSINESS_BALANCE_NOT);
                        }
                    }
                }
            }
            //新增订单关闭消息
            CereNotice cereNotice = new CereNotice();
            cereNotice.setNoticeType(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode());
            cereNotice.setJump(IntegerEnum.NOTICE_JUMP_ORDER.getCode());
            cereNotice.setBuyerUserId(order.getBuyerUserId());
            cereNotice.setShopId(order.getShopId());
            cereNotice.setNoticeTitle(StringEnum.NOTICE_TITLE_ORDER_CANCEL.getCode());
            cereNotice.setNoticeContent("您未在规定时间完成付款，订单" + order.getOrderFormid() + "已关闭,点击查看详情");
            cereNotice.setOnly(order.getOrderId());
            cereNotice.setCreateTime(time);
            cereNotice.setIfRead(IntegerEnum.NO.getCode());
            cereNoticeService.insert(cereNotice);
        }
    }

    @Override
    public CereShopOrder findById(Long orderId) {
        return cereShopOrderDAO.selectByPrimaryKey(orderId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereShopOrderDAO.updateBuyerData(buyerUserId, id);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void addComment(CommentSaveParam commentParam, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        if (!EmptyUtils.isEmpty(commentParam.getParams())) {
            //查询敏感词设置
            CerePlatformSensitive sensitive = cerePlatformSensitiveService.findSensitive();
            for (OrderCommentParam param : commentParam.getParams()) {
                CereShopComment oldComment = cereShopCommentService.findComment(user.getBuyerUserId(), param.getOrderId(), param.getSkuId());
                if (oldComment != null) {
                    throw new CoBusinessException(CoReturnFormat.ALREADY_COMMENTED);
                }
                //查询店铺数据
                CereShopComment cereShopComment = cereShopCommentService.findShop(param.getProductId());
                if (cereShopComment != null) {
                    cereShopComment.setBuyerUserId(user.getBuyerUserId());
                    cereShopComment.setCreateTime(time);
                    cereShopComment.setUpdateTime(time);
                    cereShopComment.setComment(param.getComment());
                    cereShopComment.setImage(param.getImage());
                    cereShopComment.setState(IntegerEnum.NO.getCode());
                    cereShopComment.setIfSensitive(IntegerEnum.NO.getCode());
                    cereShopComment.setOrderId(param.getOrderId());
                    cereShopComment.setSkuId(param.getSkuId());
                    cereShopComment.setStar(param.getStar());
                    cereShopComment.setDes(param.getDes());
                    cereShopComment.setDelivery(param.getDelivery());
                    cereShopComment.setAttitude(param.getAttitude());
                    cereShopComment.setImpression(param.getImpression());
                    if (sensitive != null && IntegerEnum.YES.getCode().equals(sensitive.getState())) {
                        //校验是否存在敏感词
                        String[] split = sensitive.getSensitiveWord().split(",");
                        for (String word : split) {
                            if (param.getComment().contains(word)) {
                                //如果存在敏感词
                                if (IntegerEnum.SENSITIVE_STOP.getCode().equals(sensitive.getHandleMeasures())) {
                                    throw new CoBusinessException(CoReturnFormat.SENSITIVE_ERROR);
                                } else {
                                    //设置评论敏感词状态为需审核
                                    cereShopComment.setIfSensitive(IntegerEnum.YES.getCode());
                                    cereShopComment.setState(IntegerEnum.YES.getCode());
                                }
                            }
                        }
                    }
                    cereShopCommentService.insert(cereShopComment);
                    //查询是否匹配关键词
                    List<CerePlatformWord> words = cereShopCommentService.findWords();
                    if (!EmptyUtils.isEmpty(words)) {
                        List<CereCommentWord> collect = words.stream()
                                .map(word -> {
                                    if (cereShopComment.getComment().contains(word.getKeyWord())) {
                                        CereCommentWord cereCommentWord = new CereCommentWord();
                                        cereCommentWord.setCommentId(cereShopComment.getCommentId());
                                        cereCommentWord.setProductId(cereShopComment.getProductId());
                                        cereCommentWord.setKeyWord(word.getKeyWord());
                                        cereCommentWord.setWordId(word.getWordId());
                                        return cereCommentWord;
                                    }
                                    return null;
                                }).filter(Objects::nonNull).collect(Collectors.toList());
                        if (!EmptyUtils.isEmpty(collect)) {
                            //批量插入关键词关联评论数据
                            cereCommentWordService.insertBatch(collect);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void addToComment(CommentSaveParam saveParam, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        if (!EmptyUtils.isEmpty(saveParam.getParams())) {
            //查询敏感词设置
            CerePlatformSensitive sensitive = cerePlatformSensitiveService.findSensitive();
            for (OrderCommentParam param : saveParam.getParams()) {
                //追加评论
                CereShopComment cereShopComment = new CereShopComment();
                cereShopComment.setCommentId(param.getCommentId());
                cereShopComment.setAddComment(param.getComment());
                cereShopComment.setAddImage(param.getImage());
                cereShopComment.setAddTime(time);
                cereShopComment.setUpdateTime(time);
                //查询敏感词设置
                if (sensitive != null && IntegerEnum.YES.getCode().equals(sensitive.getState())) {
                    //校验是否存在敏感词
                    String[] split = sensitive.getSensitiveWord().split(",");
                    for (String word : split) {
                        if (param.getComment().contains(word)) {
                            //如果存在敏感词
                            if (IntegerEnum.SENSITIVE_STOP.getCode().equals(sensitive.getHandleMeasures())) {
                                throw new CoBusinessException(CoReturnFormat.SENSITIVE_ERROR);
                            } else {
                                //设置评论敏感词状态为需审核
                                cereShopComment.setIfSensitive(IntegerEnum.YES.getCode());
                                cereShopComment.setState(IntegerEnum.YES.getCode());
                            }
                        }
                    }
                }
                cereShopCommentService.update(cereShopComment);
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void delete(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException {
        //校验当前订单状态是否为已完成或者已取消
        CereShopOrder cereShopOrder = cereShopOrderDAO.checkStateFinishAndCancel(param.getOrderId());
        if (cereShopOrder == null) {
            throw new CoBusinessException(CoReturnFormat.ORDER_NOT_TAKE);
        }
        //查询是否有售后数据
        List<CereOrderAfter> afters = cereShopOrderDAO.findAfterByOrderId(param.getOrderId());
        if (!EmptyUtils.isEmpty(afters)) {
            //删除售后商品属性数据
            cereShopOrderDAO.deleteAfterProductSkus(afters);
            //删除售后商品数据
            cereShopOrderDAO.deleteAfterProducts(afters);
        }
        //查询订单关联的所有消息id
        List<Long> noticeIds = cereNoticeService.findIdsByOrderId(param.getOrderId());
        if (!EmptyUtils.isEmpty(noticeIds)) {
            //删除消息数据
            cereNoticeService.deleteByIds(noticeIds);
            //删除客户关联消息数据
            cereNoticeService.deleteBuyerNotice(noticeIds, user.getBuyerUserId());
        }
        //删除订单商品属性数据
        cereShopOrderDAO.deleteOrderProductSkus(param.getOrderId());
        //删除订单关联所有数据
        cereShopOrderDAO.deleteAll(param.getOrderId());
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void returnExpress(CereAfterDilever dilever, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        dilever.setCreateTime(time);
        cereAfterDileverService.insert(dilever);
        //修改售后单状态为退货中
        CereOrderAfter cereOrderAfter = new CereOrderAfter();
        cereOrderAfter.setAfterId(dilever.getAfterId());
        cereOrderAfter.setAfterState(IntegerEnum.AFTER_RETURN_STAY.getCode());
        cereOrderAfter.setUpdateTime(time);
        cereOrderAfterService.update(cereOrderAfter);
        //新增日志
        cerePlatformLogService.addLog(user, "售后管理", "客户端操作", "填写退货物流信息", dilever.getAfterId(), time);
    }

    @Override
    public List<Dilevery> getDilevery(DileveryParam param) throws CoBusinessException, Exception {
        List<Dilevery> list = new ArrayList<>();
        //查询物流查询策略
        Integer express = cerePlatformExpressService.findExpress();
        String code = "";
        if (IntegerEnum.EXPRESS_100.getCode().equals(express)) {
            //查询快递公司编码
            code = cerePlatformDictService.findByCompany(param.getExpress(), LongEnum.EXPRESS_100.getCode());
            //通过快递100查询物流轨迹
            List<ShippingTrace> traces = kuaiDi100Service.findTraces(code, param.getDeliverFormid());
            if (!EmptyUtils.isEmpty(traces)) {
                list = traces.stream()
                        .map(a -> {
                            Dilevery dilevery = new Dilevery();
                            dilevery.setReason(a.getAcceptStation());
                            dilevery.setTime(a.getAcceptTime());
                            return dilevery;
                        }).collect(Collectors.toList());
            }
        } else if (IntegerEnum.EXPRESS_NIAO.getCode().equals(express)) {
            //查询快递公司编码
            code = cerePlatformDictService.findByCompany(param.getExpress(), LongEnum.EXPRESS_NIAO.getCode());
            //通过快递鸟查询物流轨迹
            List<ShippingTrace> traces = kuaiDiNiaoService.findTraces(code, param.getDeliverFormid());
            if (!EmptyUtils.isEmpty(traces)) {
                list = traces.stream()
                        .map(a -> {
                            Dilevery dilevery = new Dilevery();
                            dilevery.setReason(a.getAcceptStation());
                            dilevery.setTime(a.getAcceptTime());
                            return dilevery;
                        }).collect(Collectors.toList());
            }
        }
        return list;
    }

    @Override
    public void updateBatchStock(List<CereProductSku> skus) throws CoBusinessException {
        cereShopOrderDAO.updateBatchStock(skus);
    }

    private void addPayLog(CereShopOrder order, String time, String orderNo, String transactionId) throws Exception {
        CerePayLog payLog = new CerePayLog();
        payLog.setCreateTime(time);
        payLog.setOrderFormid(order.getOrderFormid());
        payLog.setOutTradeNo(orderNo);
        //设置退款单号
        if (orderNo.contains("XCX")) {
            //小程序支付
            payLog.setOutRefundNo("XCXTK" + TimeUtils.todayTime() + order.getOrderFormid() + order.getBuyerUserId());
        } else if (orderNo.contains("H5")) {
            //H5支付
            payLog.setOutRefundNo("H5TK" + TimeUtils.todayTime() + order.getOrderFormid() + order.getBuyerUserId());
        } else if (orderNo.contains("APPV3")) {
            //APPV3支付
            payLog.setOutRefundNo("APPV3TK" + TimeUtils.todayTime() + order.getOrderFormid() + order.getBuyerUserId());
        } else if (orderNo.contains("APP")) {
            //APP支付
            payLog.setOutRefundNo("APPTK" + TimeUtils.todayTime() + order.getOrderFormid() + order.getBuyerUserId());
        }
        payLog.setTransactionId(transactionId);
        payLog.setPaymentMode(order.getPaymentMode());
        payLog.setShopId(order.getShopId());
        payLog.setState(StringEnum.PAY_LOG_PAY.getCode());
        payLog.setTotalFee(order.getPrice());
        payLog.setUserId(String.valueOf(order.getBuyerUserId()));
        String payTypeStr = PayUtil.getPayTypeStr(order.getPaymentMode());
        payLog.setRemark(order.getCustomerName() + "使用" + payTypeStr + "支付了" + order.getPrice() + "元,支付订单号为：" + order.getOrderFormid()
                + ",支付时间为" + time);
        cerePayLogService.insert(payLog);
    }

    private void addReconciliation(CereShopOrder order, String time) throws Exception {
        CereOrderReconciliation reconciliation = new CereOrderReconciliation();
        reconciliation.setCreateTime(time);
        reconciliation.setMoney(order.getPrice());
        reconciliation.setOrderId(order.getOrderId());
        reconciliation.setState(IntegerEnum.RELATIONSHIP_FROZEN.getCode());
        reconciliation.setType(IntegerEnum.RELATIONSHIP_INCOME.getCode());
        cereOrderReconciliationService.insert(reconciliation);
    }

    private void addDistributor(ShopDistributor distributor, CereShopOrder order, String time, List<CereDistributionOrder> distributionOrders) throws Exception {
        BigDecimal hundred = new BigDecimal(100);
        if (!EmptyUtils.isEmpty(distributor.getDirectProportion())) {
            //计算直接分佣佣金=订单金额*直接分佣比例
            BigDecimal divide = new BigDecimal(distributor.getDirectProportion()).divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
            order.setDirectDistributorMoney(order.getPrice().multiply(divide).setScale(2, BigDecimal.ROUND_HALF_UP));
            //新增直接分销订单
            CereDistributionOrder distributionOrder = new CereDistributionOrder();
            distributionOrder.setCommission(order.getDirectDistributorMoney());
            distributionOrder.setDistributorId(distributor.getDistributorId());
            distributionOrder.setDistributorName(distributor.getDistributorName());
            distributionOrder.setDistributorPhone(distributor.getDistributorPhone());
            distributionOrder.setOrderFormid(order.getOrderFormid());
            distributionOrder.setOrderId(order.getOrderId());
            distributionOrder.setPrice(order.getPrice());
            distributionOrder.setState(IntegerEnum.NO.getCode());
            distributionOrder.setType(IntegerEnum.DIRECT_TYPE.getCode());
            distributionOrder.setTransactionTime(time);
            distributionOrders.add(distributionOrder);
        }
        if (!EmptyUtils.isEmpty(distributor.getInvitees()) && !EmptyUtils.isEmpty(distributor.getIndirectProportion())) {
            //计算间接分佣佣金=订单金额*间接分佣比例
            BigDecimal divide = new BigDecimal(distributor.getIndirectProportion()).divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
            order.setIndirectDistributorMoney(order.getPrice().multiply(divide).setScale(2, BigDecimal.ROUND_HALF_UP));
            //新增间接分佣订单
            CereDistributionOrder distributionOrder = new CereDistributionOrder();
            distributionOrder.setCommission(order.getIndirectDistributorMoney());
            distributionOrder.setDistributorId(distributor.getInvitees());
            distributionOrder.setDistributorName(distributor.getInviteesName());
            distributionOrder.setDistributorPhone(distributor.getInviteesPhone());
            distributionOrder.setOrderFormid(order.getOrderFormid());
            distributionOrder.setOrderId(order.getOrderId());
            distributionOrder.setPrice(order.getPrice());
            distributionOrder.setState(IntegerEnum.NO.getCode());
            distributionOrder.setType(IntegerEnum.IN_DIRECT_TYPE.getCode());
            distributionOrder.setTransactionTime(time);
            distributionOrders.add(distributionOrder);
        }
    }

    private void setActivityInfo(CereOrderProduct op, CartSku sku) {
        if (sku.getActivityType() != null && sku.getActivityType() > 0) {
            op.setActivityType(sku.getActivityType());
            op.setActivityId(sku.getActivityId());
        } else {
            if (sku.getPlatformSeckillId() != null && sku.getPlatformSeckillId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode());
                op.setActivityId(sku.getPlatformSeckillId());
            } else if (sku.getPlatformDiscountId() != null && sku.getPlatformDiscountId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode());
                op.setActivityId(sku.getPlatformDiscountId());
            } else if (sku.getShopSeckillId() != null && sku.getShopSeckillId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode());
                op.setActivityId(sku.getShopSeckillId());
            } else if (sku.getShopDiscountId() != null && sku.getShopDiscountId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode());
                op.setActivityId(sku.getShopDiscountId());
            } else if (sku.getShopGroupWorkId() != null && sku.getShopGroupWorkId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_GROUP.getCode());
                op.setActivityId(sku.getShopGroupWorkId());
            } else if (sku.getComposeId() != null && sku.getComposeId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_COMPOSE.getCode());
                op.setActivityId(sku.getComposeId());
            } else if (sku.getPriceId() != null && sku.getPriceId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_PRICE.getCode());
                op.setActivityId(sku.getPriceId());
            } else if (sku.getSceneId() != null && sku.getSceneId() > 0) {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_SCENE.getCode());
                op.setActivityId(sku.getSceneId());
            } else {
                op.setActivityType(IntegerEnum.ACTIVITY_TYPE_NORMAL.getCode());
            }
        }
    }

    /**
     * 结算逻辑查询实时sku信息表
     * @param settlement
     * @param param
     * @param user
     * @param coupons
     * @throws CoBusinessException
     */
    private void getSettlementInnerForRealInfo(Settlement settlement, SettlementParam param, CereBuyerUser user, List<ProductCoupon> coupons) throws CoBusinessException {
        //定义是否提示
        AtomicBoolean flag = new AtomicBoolean(false);
        //封装商品明细数据
        if (!EmptyUtils.isEmpty(param.getShops())) {
            List<SettlementShop> invalidShops = new ArrayList<>();
            List<SettlementShop> shopSettlementList = new ArrayList<>();

            List<Long> shopIdList = new ArrayList<>();
            List<Long> skuIdList = new ArrayList<>();
            for (ShopProductParam shop : param.getShops()) {
                shopIdList.add(shop.getShopId());
                skuIdList.addAll(shop.getSkus().stream().map(ProductSku::getSkuId).collect(Collectors.toList()));
            }
            Long memberLevelId = 0L;
            if (user != null && user.getMemberLevelId() != null) {
                memberLevelId = user.getMemberLevelId();
            }
            List<CartSku> realInfoList = cereSkuMemberRealInfoDAO.findSkuListBySkuIdList(skuIdList, memberLevelId);
            Map<Long, CartSku> cartSkuMap = realInfoList.stream().collect(Collectors.toMap(CartSku::getSkuId, Function.identity()));

            // 定价捆绑
            // Map<Long, Map<Long, List<CerePriceRule>>> priceMap = cereShopPriceService.selectPriceMap(shopIdList);

            // 组合捆绑
            List<CereShopComposeDTO> composeDTOList = Collections.emptyList();
            if (ParamEnum.BUY_NOW.getCode().equals(param.getType())) {
                composeDTOList = cereShopComposeService.selectByShopIdList(shopIdList);
            }
            Map<Long, List<CereShopComposeDTO>> shopIdComposeMap = composeDTOList.stream().collect(Collectors.groupingBy(CereShopComposeDTO::getShopId));

            //商家的场景营销 优惠价
            Map<Long, DiscountDescDTO> shopIdSceneDiscountMap = new HashMap<>();
            //商家的会员价优惠
            Map<Long, DiscountDescDTO> memberDiscountPriceMap = new HashMap<>();
            //商家匹配场景营销是否包邮
            Map<Long, Boolean> sceneMarketFreeShipingMap = new HashMap<>();

            //该sku可以抵扣多少积分
            Map<Long, Integer> skuCreditMap = new HashMap<>();

            //积分开关
            boolean creditSwitch = false;
            Integer totalRemainCredit = 0;
            //默认1积分抵扣0.01
            BigDecimal creditExchangeRate = new BigDecimal("0.01");
            Map<String, CerePlatformDict> dictMap = cerePlatformDictService.getDictMap(Arrays.asList(DictConstants.CREDIT_SWITCH,
                    DictConstants.CREDIT_ORDER_AMOUNT_THRESHOLD, DictConstants.CREDIT_DEDUCT_LIMIT,
                    DictConstants.CREDIT_EXCHANGE_RATE));

            CerePlatformDict dict = dictMap.get(DictConstants.CREDIT_SWITCH);
            //一笔订单达到多少金额才能抵扣积分
            CerePlatformDict dict2 = dictMap.get(DictConstants.CREDIT_ORDER_AMOUNT_THRESHOLD);
            //一笔订单最多抵扣多少积分
            CerePlatformDict dict3 = dictMap.get(DictConstants.CREDIT_DEDUCT_LIMIT);
            //1积分抵扣多少额度
            CerePlatformDict dict4 = dictMap.get(DictConstants.CREDIT_EXCHANGE_RATE);


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

            List<Long> canUseCouponProductIdList = new ArrayList<>();

            //商家id 和 对应的定价捆绑的 商品id列表
            Map<Long, List<Long>> shopPriceProductIdListMap = new HashMap<>();

            List<CerePlatformShop> shopList = cerePlatformShopDAO.selectList(Wrappers.<CerePlatformShop>lambdaQuery().in(CerePlatformShop::getShopId, shopIdList));
            Map<Long, CerePlatformShop> shopMap = shopList.stream().collect(Collectors.toMap(CerePlatformShop::getShopId, Function.identity()));

            for (ShopProductParam shop : param.getShops()) {
                //计算某个定价id优惠了多少钱
                Map<Long, DiscountDescDTO> priceDiscountMap = new HashMap<>();
                //计算某个组合捆绑id优惠了多少钱
                Map<Long, DiscountDescDTO> composeDiscountMap = new HashMap<>();

                //满足使用优惠券条件的商品列表
                List<Long> shopCanUseCouponProductIdList = new ArrayList<>();

                CerePlatformShop cps = shopMap.get(shop.getShopId());

                //查询店铺信息
                SettlementShop settlementShop = new SettlementShop();
                settlementShop.setShopId(shop.getShopId());
                settlementShop.setShopName(cps.getShopName());
                settlementShop.setShopLogo(cps.getShopLogo());
                settlementShop.setShopAdress(cps.getShopAdress());

                List<CartSku> invalidSkus = new ArrayList<>();
                int invalidNumber = 0;
                BigDecimal total = BigDecimal.ZERO;
                //sku后面接的 优惠信息
                Map<Long, List<String>> skuDiscountInfoMap = new HashMap<>();
                settlementShop.setSkuDiscountInfoMap(skuDiscountInfoMap);

                //商品还可以抵扣多少积分
                Map<Long, Integer> productRemainCreditMap = new HashMap<>();

                Map<Long, CartSku> map = new HashMap<>();
                if (!EmptyUtils.isEmpty(shop.getSkus())) {
                    //将商品数量放到map中
                    Map<Long, Integer> numberMap = shop.getSkus().stream()
                            .collect(Collectors.toMap(ProductSku::getSkuId, ProductSku::getNumber));

                    Map<Long, Integer> productIdBuyNumberMap = new HashMap<>();

                    Set<Long> usedSkuIdSet = new HashSet<>();

                    if (!EmptyUtils.isEmpty(numberMap)) {
                        //查询规格信息数据
                        List<CartSku> skus = cereProductSkuService.findStockNumberBySkusForRealInfo(shop.getSkus());
                        if (!EmptyUtils.isEmpty(skus)) {
                            //设置商品数量
                            for (CartSku sku : skus) {
                                map.put(sku.getSkuId(), sku);
                                if (creditSwitch && IntegerEnum.YES.getCode().equals(sku.getIfCredit())) {
                                    productRemainCreditMap.put(sku.getProductId(), sku.getCreditLimit());
                                } else {
                                    productRemainCreditMap.put(sku.getProductId(), 0);
                                }

                                sku.setNumber(numberMap.get(sku.getSkuId()));
                                sku.setShopId(shop.getShopId());
                                Integer buyNum = productIdBuyNumberMap.getOrDefault(sku.getProductId(), 0);
                                productIdBuyNumberMap.put(sku.getProductId(), buyNum + sku.getNumber());
                            }

                            for (CartSku sku : skus) {
                                shopCanUseCouponProductIdList.add(sku.getProductId());

                                CartSku realInfo = cartSkuMap.get(sku.getSkuId());
                                if (realInfo == null) {
                                    throw new CoBusinessException(CoReturnFormat.HAVE_INVALID_PRODUCT);
                                }
                                sku.setActivityType(realInfo.getActivityType());

                                //匹配平台秒杀/平台折扣/商家秒杀/商家折扣
                                if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(realInfo.getActivityType())
                                        || IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(realInfo.getActivityType())
                                        || IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(realInfo.getActivityType())
                                        || IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(realInfo.getActivityType())) {
                                    usedSkuIdSet.add(sku.getSkuId());

                                    if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(realInfo.getActivityType())) {
                                        sku.setPlatformSeckillId(realInfo.getActivityId());
                                    } else if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(realInfo.getActivityType())) {
                                        sku.setPlatformDiscountId(realInfo.getActivityId());
                                    } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(realInfo.getActivityType())) {
                                        sku.setShopSeckillId(realInfo.getActivityId());
                                    } else {
                                        sku.setShopDiscountId(realInfo.getActivityId());
                                    }

                                    if (new BigDecimal(productIdBuyNumberMap.get(sku.getProductId())).compareTo(realInfo.getTotal()) > 0) {
                                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                                    }

                                    if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(realInfo.getActivityType())
                                            || IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(realInfo.getActivityType())) {
                                        ProductStockInfo stockInfo = cereStockService.getActivityProductStock(realInfo.getActivityType(), sku.getProductId());
                                        if (stockInfo == null) {
                                            throw new CoBusinessException(CoReturnFormat.HAVE_INVALID_PRODUCT);
                                        }
                                        if (productIdBuyNumberMap.get(sku.getProductId()) > stockInfo.getStockNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                                        }
                                    }

                                    if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(realInfo.getActivityType())
                                            || IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(realInfo.getActivityType())) {
                                        ProductStockInfo stockInfo = cereStockService.getActivitySkuStock(realInfo.getActivityType(), sku.getSkuId());
                                        if (stockInfo == null) {
                                            throw new CoBusinessException(CoReturnFormat.HAVE_INVALID_PRODUCT);
                                        }
                                        if (productIdBuyNumberMap.get(sku.getProductId()) > stockInfo.getStockNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                                        }
                                    }

                                    sku.setOriginalPrice(realInfo.getOriginalPrice());
                                    sku.setPrice(realInfo.getPrice());

                                    //不允许叠加优惠券
                                    if (IntegerEnum.NO.getCode().equals(realInfo.getIfAdd())) {
                                        shopCanUseCouponProductIdList.remove(sku.getProductId());
                                    }
                                }


                                Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                if (totalRemainCredit > 0 && remainCredit > 0) {
                                    BigDecimal price = sku.getPrice().multiply(new BigDecimal(sku.getNumber()));
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
                            }

                            List<CartSku> skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配定价捆绑
                            matchPriceCombine(shop.getShopId(), skus, skuList, settlementShop, usedSkuIdSet, priceDiscountMap);

                            skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配组合捆绑
                            matchComposeCombine(shop.getShopId(), skuList, usedSkuIdSet, shopIdComposeMap, composeDiscountMap, numberMap);

                            skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配场景营销
                            matchSceneMarket(shop.getShopId(), user, skuList, usedSkuIdSet, shopIdSceneDiscountMap, sceneMarketFreeShipingMap);

                            skuList = skuList.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            //匹配会员价
                            matchMemberForRealInfo(skuList, usedSkuIdSet, cartSkuMap);

                            for (CartSku sku : skus) {
                                //设置规格属性值数组
                                sku.setValues(EmptyUtils.getImages(sku.getValue()));

                                sku.setTotal(new BigDecimal(sku.getNumber()).multiply(sku.getPrice()));
                                sku.setSelected(IntegerEnum.YES.getCode());
                                //校验库存
                                int stockNumber = map.get(sku.getSkuId()).getStockNumber();
                                if (sku.getNumber() > stockNumber) {
                                    //叠加商品件数
                                    invalidNumber += sku.getNumber();
                                    flag.set(true);
                                    invalidSkus.add(sku);
                                }
                            }
                            settlementShop.setNumber(skus.stream().mapToInt(CartSku::getNumber).sum());

                            List<CartSku> sortSkuList = new ArrayList<>();

                            if (priceDiscountMap.size() > 0) {
                                for (Long id : priceDiscountMap.keySet()) {
                                    DiscountDescDTO value = priceDiscountMap.get(id);

                                    List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getPriceId() != null && sku.getPriceId().equals(id)).collect(Collectors.toList());
                                    sortSkuList.addAll(tmpSkuList);
                                    BigDecimal tmpTotal = BigDecimal.ZERO;

                                    for (CartSku sku : tmpSkuList) {
                                        total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                        tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    }

                                    shopPriceProductIdListMap.put(shop.getShopId(), tmpSkuList.stream().map(CartSku::getProductId).distinct().collect(Collectors.toList()));

                                    total = total.subtract(value.getDiscountTotal());
                                    settlementShop.setPriceAfterDiscount(tmpTotal.subtract(value.getDiscountTotal()));

                                    for (CartSku sku : tmpSkuList) {
                                        Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                        if (totalRemainCredit > 0 && remainCredit > 0) {
                                            BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                            int needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
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
                                    }

                                    skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("定价捆绑  %.2f元任选%d件 x %d", value.getComposePrice(), value.getComposeNum(), value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                                }
                            }

                            if (composeDiscountMap.size() > 0) {
                                for (Long id : composeDiscountMap.keySet()) {
                                    DiscountDescDTO value = composeDiscountMap.get(id);

                                    List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getComposeId() != null && sku.getComposeId().equals(id)).collect(Collectors.toList());
                                    sortSkuList.addAll(tmpSkuList);
                                    BigDecimal tmpTotal = BigDecimal.ZERO;

                                    for (CartSku sku : tmpSkuList) {
                                        total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                        tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    }
                                    total = total.subtract(value.getDiscountTotal());

                                    for (CartSku sku : tmpSkuList) {
                                        Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                        if (totalRemainCredit > 0 && remainCredit > 0) {
                                            BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                            int needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
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
                                    }

                                    skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("组合捆绑  x %d", value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                                }
                            }

                            if (shopIdSceneDiscountMap.get(shop.getShopId()) != null) {
                                DiscountDescDTO value = shopIdSceneDiscountMap.get(shop.getShopId());

                                List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getSceneId() != null).collect(Collectors.toList());

                                BigDecimal tmpTotal = BigDecimal.ZERO;
                                for (CartSku sku : tmpSkuList) {
                                    tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }

                                for (CartSku sku : tmpSkuList) {
                                    Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                    if (totalRemainCredit > 0 && remainCredit > 0) {
                                        BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                        int needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
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
                                }

                                skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("场景营销  %.1f折 x %d", value.getDiscount(), value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                            }

                            if (memberDiscountPriceMap.get(shop.getShopId()) != null) {
                                DiscountDescDTO value = memberDiscountPriceMap.get(shop.getShopId());

                                List<CartSku> tmpSkuList = skus.stream().filter(CartSku::isUseMember).collect(Collectors.toList());
                                sortSkuList.addAll(tmpSkuList);

                                BigDecimal tmpTotal = BigDecimal.ZERO;

                                for (CartSku sku : tmpSkuList) {
                                    total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }
                                total = total.subtract(value.getDiscountTotal());

                                for (CartSku sku : tmpSkuList) {
                                    Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                    if (totalRemainCredit > 0 && remainCredit > 0) {
                                        BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                        int needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
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
                                }

                                skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("会员价 x %d", value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                            }

                            //剩下的就是普通的商品
                            skus.removeAll(sortSkuList);
                            for (CartSku sku : skus) {
                                total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));

                                Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                if (totalRemainCredit > 0 && remainCredit > 0) {
                                    BigDecimal price = sku.getPrice().multiply(new BigDecimal(sku.getNumber()));
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
                            }
                            sortSkuList.addAll(skus);
                            settlementShop.setSkus(sortSkuList);

                            if (flag.get()) {
                                SettlementShop settlementShop1 = new SettlementShop();
                                settlementShop1.setShopId(shop.getShopId());
                                settlementShop1.setNumber(invalidNumber);
                                settlementShop1.setSkus(invalidSkus);
                                invalidShops.add(settlementShop1);
                            } else {
                                //查询店铺物流方案
                                List<CereOrderLogistics> logistics = cereOrderLogisticsService.findLogistics(shop.getShopId());
                                //计算物流费用 如果场景营销中有包邮，则把包邮的sku过滤掉
                                List<CartSku> logisticsSkuList = sortSkuList;
                                if (sceneMarketFreeShipingMap.get(shop.getShopId()) != null) {
                                    logisticsSkuList = sortSkuList.stream().filter(sku -> sku.getSceneId() == null).collect(Collectors.toList());
                                }
                                Distribution distribution = setLogisticPrice(logistics, logisticsSkuList, settlement.getReceive(), numberMap);
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
                            }
                        }
                    }
                }
                settlementShop.setTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));

                canUseCouponProductIdList.addAll(shopCanUseCouponProductIdList);

                //设置商家优惠券
                if (CollectionUtils.isNotEmpty(shopCanUseCouponProductIdList)) {
                    List<ProductCoupon> couponList = cereBuyerShopCouponService.findCouponMatchCondition(user.getBuyerUserId(), settlementShop.getTotal(), shopCanUseCouponProductIdList);
                    couponList.removeIf(pc -> !reMatchCoupon(pc, Collections.singletonList(settlementShop), shopPriceProductIdListMap));
                    couponList.forEach(this::assembleContent);
                    settlementShop.setShopCoupons(couponList);
                }

                shopSettlementList.add(settlementShop);
            }

            if (!canUseCouponProductIdList.isEmpty()) {
                //整个订单最高价 只需要查询优惠券满额上限在这个一下的就可以了
                BigDecimal totalPrice = shopSettlementList.stream().map(SettlementShop::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
                List<ProductCoupon> couponList = cereBuyerCouponService.findCouponMatchCondition(user.getBuyerUserId(), totalPrice, canUseCouponProductIdList);
                //分别判断每个优惠券,是否完全匹配商家的定价捆绑
                if (CollectionUtils.isNotEmpty(couponList)) {
                    couponList.removeIf(pc -> !reMatchCoupon(pc, shopSettlementList, shopPriceProductIdListMap));
                    couponList.forEach(this::assembleContent);
                }
                settlement.setCoupons(couponList);
            }

            settlement.setShops(shopSettlementList);
            settlement.setInvalidShops(invalidShops);
            if (flag.get()) {
                //超出库存,提示‘存在无效商品，请重新选择’
                throw new CoBusinessException(CoReturnFormat.HAVE_INVALID_PRODUCT, settlement);
            }
        }
    }

    /**
     * 结算具体逻辑
     *
     * @param settlement
     * @param param
     * @param user
     * @param coupons
     * @throws CoBusinessException
     */
    private void getSettlementInner(Settlement settlement, SettlementParam param, CereBuyerUser user, List<ProductCoupon> coupons) throws CoBusinessException {
        //定义是否提示
        AtomicBoolean flag = new AtomicBoolean(false);
        //封装商品明细数据
        if (!EmptyUtils.isEmpty(param.getShops())) {
            List<SettlementShop> invalidShops = new ArrayList<>();
            List<SettlementShop> shopSettlementList = new ArrayList<>();

            //查找所有的活动相关信息
            //cerePlatformSeckillService.
            List<Long> shopIdList = new ArrayList<>();
            for (ShopProductParam shop : param.getShops()) {
                shopIdList.add(shop.getShopId());
            }

            // 平台秒杀
            List<ShopPlatformSeckill> platformSeckillList = cerePlatformSeckillService.selectPlatformSeckillsByShopIdList(shopIdList);
            Map<Long, List<ShopPlatformSeckill>> productIdPlatformSeckillMap = platformSeckillList.stream().collect(Collectors.groupingBy(ShopPlatformSeckill::getProductId));

            // 平台折扣
            List<ShopPlatformDiscount> platformDiscountList = cerePlatformDiscountService.selectPlatformDiscountByShopIdList(shopIdList);
            Map<Long, List<ShopPlatformDiscount>> productIdPlatformDiscountMap = platformDiscountList.stream().collect(Collectors.groupingBy(ShopPlatformDiscount::getProductId));

            // 商家秒杀
            List<ShopBusinessSeckill> seckillList = cereShopSeckillService.selectByShopIdList(shopIdList);
            Map<Long, List<ShopBusinessSeckill>> skuIdSeckillMap = seckillList.stream().collect(Collectors.groupingBy(ShopBusinessSeckill::getSkuId));

            // 商家折扣
            List<ShopBusinessDiscount> discountList = cereShopDiscountService.selectByShopIdList(shopIdList);
            Map<Long, List<ShopBusinessDiscount>> skuIdDiscountMap = discountList.stream().collect(Collectors.groupingBy(ShopBusinessDiscount::getSkuId));

            // 定价捆绑
            // Map<Long, Map<Long, List<CerePriceRule>>> priceMap = cereShopPriceService.selectPriceMap(shopIdList);

            // 组合捆绑
            List<CereShopComposeDTO> composeDTOList = Collections.emptyList();
            if (ParamEnum.BUY_NOW.getCode().equals(param.getType())) {
                composeDTOList = cereShopComposeService.selectByShopIdList(shopIdList);
            }
            Map<Long, List<CereShopComposeDTO>> shopIdComposeMap = composeDTOList.stream().collect(Collectors.groupingBy(CereShopComposeDTO::getShopId));

            //商家的场景营销 优惠价
            Map<Long, DiscountDescDTO> shopIdSceneDiscountMap = new HashMap<>();
            //商家的会员价优惠
            Map<Long, DiscountDescDTO> memberDiscountPriceMap = new HashMap<>();
            //商家匹配场景营销是否包邮
            Map<Long, Boolean> sceneMarketFreeShipingMap = new HashMap<>();

            //该sku可以抵扣多少积分
            Map<Long, Integer> skuCreditMap = new HashMap<>();

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

            List<Long> canUseCouponProductIdList = new ArrayList<>();

            //商家id 和 对应的定价捆绑的 商品id列表
            Map<Long, List<Long>> shopPriceProductIdListMap = new HashMap<>();

            for (ShopProductParam shop : param.getShops()) {

                //计算某个定价id优惠了多少钱
                Map<Long, DiscountDescDTO> priceDiscountMap = new HashMap<>();
                //计算某个组合捆绑id优惠了多少钱
                Map<Long, DiscountDescDTO> composeDiscountMap = new HashMap<>();

                //满足使用优惠券条件的商品列表
                List<Long> shopCanUseCouponProductIdList = new ArrayList<>();

                //查询店铺信息
                SettlementShop settlementShop = cereShopOrderDAO.findSettlementShop(shop.getShopId());
                settlementShop.setShopId(shop.getShopId());
                List<CartSku> invalidSkus = new ArrayList<>();
                int invalidNumber = 0;
                BigDecimal total = BigDecimal.ZERO;
                //sku后面接的 优惠信息
                Map<Long, List<String>> skuDiscountInfoMap = new HashMap<>();
                settlementShop.setSkuDiscountInfoMap(skuDiscountInfoMap);

                //商品还可以抵扣多少积分
                Map<Long, Integer> productRemainCreditMap = new HashMap<>();

                Map<Long, CartSku> map = new HashMap<>();
                if (!EmptyUtils.isEmpty(shop.getSkus())) {
                    //查询当前店铺所有购买商品的库存数据
                    List<CartSku> productSkus = cereProductSkuService.findStockNumberBySkus(shop.getSkus());
                    if (!EmptyUtils.isEmpty(productSkus)) {
                        for (CartSku sku : productSkus) {
                            map.put(sku.getSkuId(), sku);
                            if (creditSwitch && IntegerEnum.YES.getCode().equals(sku.getIfCredit())) {
                                productRemainCreditMap.put(sku.getProductId(), sku.getCreditLimit());
                            } else {
                                productRemainCreditMap.put(sku.getProductId(), 0);
                            }
                        }
                    }
                    //将商品数量放到map中
                    Map<Long, Integer> numberMap = shop.getSkus().stream()
                            .collect(Collectors.toMap(ProductSku::getSkuId, ProductSku::getNumber));

                    Map<Long, Integer> productIdBuyNumberMap = new HashMap<>();

                    Set<Long> usedSkuIdSet = new HashSet<>();

                    if (!EmptyUtils.isEmpty(numberMap)) {
                        //查询规格信息数据
                        List<CartSku> skus = cereProductSkuService.findStockNumberBySkus(shop.getSkus());
                        if (!EmptyUtils.isEmpty(skus)) {
                            //设置商品数量
                            skus.forEach(sku -> {
                                sku.setNumber(numberMap.get(sku.getSkuId()));
                                sku.setShopId(shop.getShopId());
                                Integer buyNum = productIdBuyNumberMap.getOrDefault(sku.getProductId(), 0);
                                productIdBuyNumberMap.put(sku.getProductId(), buyNum + sku.getNumber());
                            });

                            /*for (CartSku sku : skus) {
                                //秒杀活动和限时折扣活动校验
                                //checkActivity(param, numberMap.get(sku.getSkuId()), sku, settlement, user, shop);
                                //设置规格属性值数组
                                sku.setValues(EmptyUtils.getImages(sku.getValue()));
                            }*/

                            for (CartSku sku : skus) {

                                shopCanUseCouponProductIdList.add(sku.getProductId());

                                List<ShopPlatformSeckill> shopPlatformSeckillList = productIdPlatformSeckillMap.get(sku.getProductId());
                                List<ShopPlatformDiscount> shopPlatformDiscountList = productIdPlatformDiscountMap.get(sku.getProductId());
                                List<ShopBusinessSeckill> shopBusinessSeckillList = skuIdSeckillMap.get(sku.getSkuId());
                                List<ShopBusinessDiscount> shopBusinessDiscountList = skuIdDiscountMap.get(sku.getSkuId());
                                BigDecimal num = new BigDecimal(sku.getNumber());

                                //匹配平台秒杀
                                if (CollectionUtils.isNotEmpty(shopPlatformSeckillList)) {
                                    ShopPlatformSeckill seckill = shopPlatformSeckillList.get(0);

                                    usedSkuIdSet.add(sku.getSkuId());
                                    sku.setPlatformSeckillId(shopPlatformSeckillList.get(0).getSeckillId());
                                    if (sku.getOriginalPrice().compareTo(seckill.getSeckillMoney()) >= 0) {
                                        sku.setPrice(sku.getOriginalPrice().subtract(seckill.getSeckillMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));

                                        if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(seckill.getIfLimit())) {
                                            if (productIdBuyNumberMap.get(sku.getProductId()) > seckill.getLimitNumber()) {
                                                throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                                            }
                                        }
                                        if (seckill.getNumber() != null && productIdBuyNumberMap.get(sku.getProductId()) > seckill.getNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                                        }
                                        //不允许叠加优惠券
                                        if (IntegerEnum.NO.getCode().equals(seckill.getIfAdd())) {
                                            shopCanUseCouponProductIdList.remove(sku.getProductId());
                                        }
                                    }
                                }

                                //匹配平台折扣
                                if (CollectionUtils.isNotEmpty(shopPlatformDiscountList)) {
                                    ShopPlatformDiscount discount = shopPlatformDiscountList.get(0);

                                    usedSkuIdSet.add(sku.getSkuId());
                                    sku.setPlatformDiscountId(shopPlatformDiscountList.get(0).getDiscountId());
                                    sku.setPrice(sku.getOriginalPrice().multiply(discount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));

                                    if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(discount.getIfLimit())) {
                                        if (productIdBuyNumberMap.get(sku.getProductId()) > discount.getLimitNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                                        }
                                    }
                                    if (discount.getNumber() != null && productIdBuyNumberMap.get(sku.getProductId()) > discount.getNumber()) {
                                        throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                                    }
                                    //不允许叠加优惠券
                                    if (IntegerEnum.NO.getCode().equals(discount.getIfAdd())) {
                                        shopCanUseCouponProductIdList.remove(sku.getProductId());
                                    }
                                }

                                //匹配商家秒杀
                                if (CollectionUtils.isNotEmpty(shopBusinessSeckillList)) {
                                    ShopBusinessSeckill seckill = shopBusinessSeckillList.get(0);

                                    usedSkuIdSet.add(sku.getSkuId());
                                    sku.setShopSeckillId(shopBusinessSeckillList.get(0).getSeckillId());
                                    sku.setPrice(seckill.getSeckillPrice());

                                    if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(seckill.getIfLimit())) {
                                        if (seckill.getLimitNumber() != null && productIdBuyNumberMap.get(sku.getProductId()) > seckill.getLimitNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                                        }
                                    }
                                    if (IntegerEnum.NO.getCode().equals(seckill.getIfAdd())) {
                                        shopCanUseCouponProductIdList.remove(sku.getProductId());
                                    }
                                }

                                //匹配商家折扣
                                if (CollectionUtils.isNotEmpty(shopBusinessDiscountList)) {
                                    ShopBusinessDiscount discount = shopBusinessDiscountList.get(0);

                                    usedSkuIdSet.add(sku.getSkuId());
                                    sku.setShopDiscountId(discount.getDiscountId());
                                    sku.setPrice(discount.getPrice());

                                    if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(discount.getIfLimit())) {
                                        if (discount.getLimitNumber() != null && productIdBuyNumberMap.get(sku.getProductId()) > discount.getLimitNumber()) {
                                            throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                                        }
                                    }
                                    if (IntegerEnum.NO.getCode().equals(discount.getIfAdd())) {
                                        shopCanUseCouponProductIdList.remove(sku.getProductId());
                                    }
                                }

                                Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                if (totalRemainCredit > 0 && remainCredit > 0) {
                                    BigDecimal price = sku.getPrice().multiply(new BigDecimal(sku.getNumber()));
                                    Integer needCredit = price.divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                    if (needCredit > 0) {
                                        Integer min = Math.min(remainCredit, totalRemainCredit);
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
                            }

                            List<CartSku> skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配定价捆绑
                            matchPriceCombine(shop.getShopId(), skus, skuList, settlementShop, usedSkuIdSet, priceDiscountMap);

                            skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配组合捆绑
                            matchComposeCombine(shop.getShopId(), skuList, usedSkuIdSet, shopIdComposeMap, composeDiscountMap, numberMap);

                            skuList = skus.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            // 匹配场景营销
                            matchSceneMarket(shop.getShopId(), user, skuList, usedSkuIdSet, shopIdSceneDiscountMap, sceneMarketFreeShipingMap);

                            skuList = skuList.stream().filter(sku -> !usedSkuIdSet.contains(sku.getSkuId())).collect(Collectors.toList());

                            //匹配会员价
                            matchMember(shop.getShopId(), user, skuList, usedSkuIdSet, memberDiscountPriceMap);

                            //针对定价捆绑，如果
                            //Map<Long, List<Long>> productIdPlatformCouponIdMap = new HashMap<>();
                            //Map<Long, List<Long>> productIdShopCouponIdMap = new HashMap<>();
                            for (CartSku sku : skus) {
                                //秒杀活动和限时折扣活动校验
                                checkActivity(param, numberMap.get(sku.getSkuId()), sku, settlement, user, shop);
                                //设置规格属性值数组
                                sku.setValues(EmptyUtils.getImages(sku.getValue()));

                                sku.setTotal(new BigDecimal(sku.getNumber()).multiply(sku.getPrice()));
                                sku.setSelected(IntegerEnum.YES.getCode());
                                //定义是否需要查询优惠券标识,默认需要
                                /*setSettlementCoupons(param, shop, user.getBuyerUserId(), sku, settlementShop, settlement,
                                        settlement.getCoupons(), settlementShop.getShopCoupons(), productIdPlatformCouponIdMap, productIdShopCouponIdMap);*/
                                //校验库存
                                int stockNumber = 0;
                                /*if(!EmptyUtils.isEmpty(stringRedisService.get(String.valueOf(sku.getSkuId())))){
                                    stockNumber=(int) stringRedisService.get(String.valueOf(sku.getSkuId()));
                                }else {
                                    stockNumber= map.get(sku.getSkuId()).getStockNumber();
                                }*/
                                stockNumber = map.get(sku.getSkuId()).getStockNumber();
                                if (sku.getNumber() > stockNumber) {
                                    //叠加商品件数
                                    invalidNumber += sku.getNumber();
                                    flag.set(true);
                                    invalidSkus.add(sku);
                                }
                                //total = total.add(sku.getTotal());
                            }
                            //settlementShop.setSkus(skus);
                            settlementShop.setNumber(skus.stream().mapToInt(CartSku::getNumber).sum());

                            List<CartSku> sortSkuList = new ArrayList<>();

                            if (priceDiscountMap.size() > 0) {
                                for (Long id : priceDiscountMap.keySet()) {
                                    DiscountDescDTO value = priceDiscountMap.get(id);

                                    List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getPriceId() != null && sku.getPriceId().equals(id)).collect(Collectors.toList());
                                    sortSkuList.addAll(tmpSkuList);
                                    BigDecimal tmpTotal = BigDecimal.ZERO;

                                    for (CartSku sku : tmpSkuList) {
                                        total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                        tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    }

                                    shopPriceProductIdListMap.put(shop.getShopId(), tmpSkuList.stream().map(CartSku::getProductId).distinct().collect(Collectors.toList()));

                                    total = total.subtract(value.getDiscountTotal());
                                    settlementShop.setPriceAfterDiscount(tmpTotal.subtract(value.getDiscountTotal()));

                                    for (CartSku sku : tmpSkuList) {
                                        Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                        if (totalRemainCredit > 0 && remainCredit > 0) {
                                            BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                            Integer needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                            if (needCredit > 0) {
                                                Integer min = Math.min(remainCredit, totalRemainCredit);
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
                                    }

                                    skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("定价捆绑  %.2f元任选%d件 x %d", value.getComposePrice(), value.getComposeNum(), value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                                }
                            }

                            if (composeDiscountMap.size() > 0) {
                                for (Long id : composeDiscountMap.keySet()) {
                                    DiscountDescDTO value = composeDiscountMap.get(id);

                                    List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getComposeId() != null && sku.getComposeId().equals(id)).collect(Collectors.toList());
                                    sortSkuList.addAll(tmpSkuList);
                                    BigDecimal tmpTotal = BigDecimal.ZERO;

                                    for (CartSku sku : tmpSkuList) {
                                        total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                        tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    }
                                    total = total.subtract(value.getDiscountTotal());

                                    for (CartSku sku : tmpSkuList) {
                                        Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                        if (totalRemainCredit > 0 && remainCredit > 0) {
                                            BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                            Integer needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                            if (needCredit > 0) {
                                                Integer min = Math.min(remainCredit, totalRemainCredit);
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
                                    }

                                    skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("组合捆绑  x %d", value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                                }
                            }

                            if (shopIdSceneDiscountMap.get(shop.getShopId()) != null) {
                                DiscountDescDTO value = shopIdSceneDiscountMap.get(shop.getShopId());

                                List<CartSku> tmpSkuList = skus.stream().filter(sku -> sku.getSceneId() != null).collect(Collectors.toList());
                                /*sortSkuList.addAll(tmpSkuList);

                                for (CartSku sku:tmpSkuList) {
                                    total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }
                                total = total.subtract(value.getDiscountTotal());*/

                                BigDecimal tmpTotal = BigDecimal.ZERO;
                                for (CartSku sku : tmpSkuList) {
                                    tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }

                                for (CartSku sku : tmpSkuList) {
                                    Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                    if (totalRemainCredit > 0 && remainCredit > 0) {
                                        BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                        Integer needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                        if (needCredit > 0) {
                                            Integer min = Math.min(remainCredit, totalRemainCredit);
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
                                }

                                skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("场景营销  %.1f折 x %d", value.getDiscount(), value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                            }

                            if (memberDiscountPriceMap.get(shop.getShopId()) != null) {
                                DiscountDescDTO value = memberDiscountPriceMap.get(shop.getShopId());

                                List<CartSku> tmpSkuList = skus.stream().filter(CartSku::isUseMember).collect(Collectors.toList());
                                sortSkuList.addAll(tmpSkuList);

                                BigDecimal tmpTotal = BigDecimal.ZERO;

                                for (CartSku sku : tmpSkuList) {
                                    total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                    tmpTotal = tmpTotal.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }
                                total = total.subtract(value.getDiscountTotal());

                                for (CartSku sku : tmpSkuList) {
                                    Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                    if (totalRemainCredit > 0 && remainCredit > 0) {
                                        BigDecimal percent = sku.getPrice().multiply(new BigDecimal(sku.getNumber())).divide(tmpTotal, 4, BigDecimal.ROUND_HALF_UP);
                                        Integer needCredit = tmpTotal.subtract(value.getDiscountTotal()).multiply(percent).divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                        if (needCredit > 0) {
                                            Integer min = Math.min(remainCredit, totalRemainCredit);
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
                                }

                                skuDiscountInfoMap.put(tmpSkuList.get(tmpSkuList.size() - 1).getSkuId(), Arrays.asList(String.format("会员价 x %d", value.getDiscountNum()), "优惠￥" + value.getDiscountTotal()));
                            }

                            //剩下的就是普通的商品
                            skus.removeAll(sortSkuList);
                            for (CartSku sku : skus) {
                                total = total.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));

                                Integer remainCredit = productRemainCreditMap.get(sku.getProductId());
                                if (totalRemainCredit > 0 && remainCredit > 0) {
                                    BigDecimal price = sku.getPrice().multiply(new BigDecimal(sku.getNumber()));
                                    Integer needCredit = price.divide(creditExchangeRate, 0, BigDecimal.ROUND_HALF_DOWN).intValue();
                                    if (needCredit > 0) {
                                        Integer min = Math.min(remainCredit, totalRemainCredit);
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
                            }
                            sortSkuList.addAll(skus);
                            settlementShop.setSkus(sortSkuList);

                            //定价捆绑查询出的商家优惠券要求同时匹配定价捆绑活动中的所有商品
                            /*if (productIdShopCouponIdMap.size() > 0) {
                                Set<Long> couponIdSet = new HashSet<>();
                                for (List<Long> couponIdList : productIdShopCouponIdMap.values()) {
                                    couponIdSet.addAll(couponIdList);
                                }
                                Collection<Long> intersection = new HashSet<>(couponIdSet);
                                for (List<Long> couponIdList : productIdShopCouponIdMap.values()) {
                                    intersection = CollectionUtils.intersection(intersection, couponIdList);
                                }
                                Collection<Long> finalIntersection = intersection;
                                settlementShop.getShopCoupons().removeIf(obj -> couponIdSet.contains(obj.getCouponId()) && !finalIntersection.contains(obj.getCouponId()));
                                for (ProductCoupon coupon : settlementShop.getShopCoupons()) {
                                    if (settlementShop.getPriceAfterDiscount() != null
                                            && coupon.getFullMoney().compareTo(settlementShop.getPriceAfterDiscount()) > 0
                                            && coupon.getIds() != null) {
                                        //商家券不满足 满X元优惠的条件，直接去掉定价捆绑商品对应的商品id列表
                                        coupon.getIds().removeAll(productIdShopCouponIdMap.keySet());
                                    }
                                }
                                //进一步判断，如果优惠券没有包含其它商品，直接移除掉这张优惠券
                                List<Long> shopProductIdList = settlementShop.getSkus().stream().map(CartSku::getProductId).collect(Collectors.toList());
                                Iterator<ProductCoupon> ite = settlementShop.getShopCoupons().iterator();
                                while (ite.hasNext()) {
                                    ProductCoupon coupon = ite.next();
                                    Collection<Long> interProductIdList = CollectionUtils.intersection(coupon.getIds(), shopProductIdList);
                                    if (CollectionUtils.isEmpty(interProductIdList)) {
                                        ite.remove();
                                    }
                                }
                            }*/

                            if (flag.get()) {
                                SettlementShop settlementShop1 = new SettlementShop();
                                settlementShop1.setShopId(shop.getShopId());
                                settlementShop1.setNumber(invalidNumber);
                                settlementShop1.setSkus(invalidSkus);
                                invalidShops.add(settlementShop1);
                            } else {
                                //查询店铺物流方案
                                List<CereOrderLogistics> logistics = cereOrderLogisticsService.findLogistics(shop.getShopId());
                                //计算物流费用 如果场景营销中有包邮，则把包邮的sku过滤掉
                                List<CartSku> logisticsSkuList = sortSkuList;
                                if (sceneMarketFreeShipingMap.get(shop.getShopId()) != null) {
                                    logisticsSkuList = sortSkuList.stream().filter(sku -> sku.getSceneId() == null).collect(Collectors.toList());
                                }
                                Distribution distribution = setLogisticPrice(logistics, logisticsSkuList, settlement.getReceive(), numberMap);
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
                            }
                        }
                    }
                }
                settlementShop.setTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));

                canUseCouponProductIdList.addAll(shopCanUseCouponProductIdList);

                //设置商家优惠券
                if (CollectionUtils.isNotEmpty(shopCanUseCouponProductIdList)) {
                    List<ProductCoupon> couponList = cereBuyerShopCouponService.findCouponMatchCondition(user.getBuyerUserId(), settlementShop.getTotal(), shopCanUseCouponProductIdList);
                    couponList.removeIf(pc -> !reMatchCoupon(pc, Collections.singletonList(settlementShop), shopPriceProductIdListMap));
                    couponList.forEach(this::assembleContent);
                    settlementShop.setShopCoupons(couponList);
                }

                shopSettlementList.add(settlementShop);
            }

            if (!canUseCouponProductIdList.isEmpty()) {
                //整个订单最高价 只需要查询优惠券满额上限在这个一下的就可以了
                BigDecimal totalPrice = shopSettlementList.stream().map(SettlementShop::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
                List<ProductCoupon> couponList = cereBuyerCouponService.findCouponMatchCondition(user.getBuyerUserId(), totalPrice, canUseCouponProductIdList);
                //分别判断每个优惠券,是否完全匹配商家的定价捆绑
                if (CollectionUtils.isNotEmpty(couponList)) {
                    couponList.removeIf(pc -> !reMatchCoupon(pc, shopSettlementList, shopPriceProductIdListMap));
                    couponList.forEach(this::assembleContent);
                }
                settlement.setCoupons(couponList);
            }

            settlement.setShops(shopSettlementList);
            settlement.setInvalidShops(invalidShops);
            if (flag.get()) {
                //超出库存,提示‘存在无效商品，请重新选择’
                throw new CoBusinessException(CoReturnFormat.HAVE_INVALID_PRODUCT, settlement);
            }
        }
    }

    /**
     * 设置展示文案
     *
     * @param pc
     */
    private void assembleContent(ProductCoupon pc) {
        if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(pc.getCouponType())) {
            pc.setContent("满" + pc.getFullMoney().stripTrailingZeros().toPlainString() +
                    "减" + pc.getReduceMoney().stripTrailingZeros().toPlainString() + "元");
        } else {
            pc.setContent(pc.getReduceMoney().stripTrailingZeros().toPlainString() + "折券");
        }
    }

    /**
     * 这里查询出来的优惠券需要判断是否匹配整个定价捆绑的商品
     *
     * @param pc
     * @param shopSettlementList
     * @param shopPriceProductIdListMap
     * @return
     */
    private boolean reMatchCoupon(ProductCoupon pc, List<SettlementShop> shopSettlementList, Map<Long, List<Long>> shopPriceProductIdListMap) {
        List<Long> ids = pc.getIds();
        BigDecimal fullPrice = BigDecimal.ZERO;
        for (SettlementShop shop : shopSettlementList) {
            List<Long> priceProductIdList = shopPriceProductIdListMap.get(shop.getShopId());
            if (CollectionUtil.isNotEmpty(priceProductIdList)) {
                if (ids.containsAll(priceProductIdList)) {
                    fullPrice = fullPrice.add(shop.getPriceAfterDiscount());
                } else {
                    pc.getIds().removeAll(priceProductIdList);
                }
            }
            for (CartSku sku : shop.getSkus()) {
                if ((sku.getPriceId() == null || sku.getPriceId() == 0) && (IntegerEnum.APPLY_TYPE_ALL.getCode().equals(pc.getApplyType()) || (ids.contains(sku.getProductId())))) {
                    fullPrice = fullPrice.add(sku.getTotal());
                }
            }
        }
        //有可能是没有设置满多少元，则默认是满0元
        if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(pc.getCouponType())) {
            if (fullPrice.compareTo(pc.getFullMoney()) > 0 && fullPrice.compareTo(pc.getReduceMoney()) > 0) {
                return true;
            }
        } else {
            BigDecimal discountPromote = pc.getReduceMoney().divide(BigDecimal.TEN);
            BigDecimal discountPrice = fullPrice.multiply(discountPromote).setScale(4, BigDecimal.ROUND_HALF_UP);
            //打折后>= 0.01则符合
            if (discountPrice.compareTo(new BigDecimal("0.01")) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配定价捆绑
     *
     * @param shopId
     * @param skus
     * @param skuList
     * @param settlementShop
     * @param usedSkuIdSet
     * @param priceDiscountMap
     */
    private void matchPriceCombine(Long shopId, List<CartSku> skus, List<CartSku> skuList, SettlementShop settlementShop, Set<Long> usedSkuIdSet, Map<Long, DiscountDescDTO> priceDiscountMap) {
        if (CollectionUtils.isNotEmpty(skuList)) {
            PriceCombineParam priceCombineParam = getPriceParam(Collections.singletonList(shopId), skus.stream().map(CartSku::getProductId).collect(Collectors.toList()));
            matchCombineExtNew(priceCombineParam, skus, skuList, settlementShop, usedSkuIdSet, priceDiscountMap);
        }
    }

    /**
     * 匹配组合捆绑
     *
     * @param shopId
     * @param skuList
     * @param usedSkuIdSet
     * @param shopIdComposeMap
     * @param composeDiscountMap
     * @param numberMap
     */
    private void matchComposeCombine(Long shopId, List<CartSku> skuList, Set<Long> usedSkuIdSet, Map<Long, List<CereShopComposeDTO>> shopIdComposeMap,
                                     Map<Long, DiscountDescDTO> composeDiscountMap, Map<Long, Integer> numberMap) {
        if (CollectionUtils.isNotEmpty(skuList)) {
            List<CereShopComposeDTO> shopComposeList = shopIdComposeMap.get(shopId);
            skuList = skuList.stream().sorted((o1, o2) -> o2.getNumber().compareTo(o1.getNumber())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(shopComposeList)) {
                // composeId - 列表
                Map<Long, List<CereShopComposeDTO>> composeIdListMap = shopComposeList.stream().collect(Collectors.groupingBy(CereShopComposeDTO::getComposeId));
                for (Map.Entry<Long, List<CereShopComposeDTO>> entry : composeIdListMap.entrySet()) {
                    Long composeId = entry.getKey();
                    List<Long> productIdList = entry.getValue().stream().map(CereShopComposeDTO::getProductId).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(productIdList)) {
                        List<Long> tmpUsedSkuIdList = new ArrayList<>();
                        for (Long productId : productIdList) {
                            for (CartSku cartSku : skuList) {
                                if (!tmpUsedSkuIdList.contains(cartSku.getSkuId()) && cartSku.getProductId().equals(productId)) {
                                    tmpUsedSkuIdList.add(cartSku.getSkuId());
                                    break;
                                }
                            }
                        }
                        //说明数量匹配上了
                        if (tmpUsedSkuIdList.size() == productIdList.size()) {
                            usedSkuIdSet.addAll(tmpUsedSkuIdList);
                            DiscountDescDTO discountDescDTO = new DiscountDescDTO();

                            Long lastSkuId = tmpUsedSkuIdList.get(tmpUsedSkuIdList.size() - 1);
                            //由于数量是由大到小排序，所以最后一个的数量代表了 可匹配次数
                            Integer lastNumber = numberMap.get(lastSkuId);

                            CereShopComposeDTO composeDTO = entry.getValue().get(0);
                            int composeType = composeDTO.getComposeType();

                            BigDecimal oldPrice = BigDecimal.ZERO;
                            for (CartSku sku : skuList) {
                                if (tmpUsedSkuIdList.contains(sku.getSkuId())) {
                                    sku.setComposeId(composeId);
                                    oldPrice = oldPrice.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                                }
                            }
                            discountDescDTO.setDiscountNum(lastNumber);
                            if (composeType == 1) {
                                BigDecimal price = composeDTO.getPromote().multiply(new BigDecimal(lastNumber));
                                discountDescDTO.setDiscountTotal(oldPrice.subtract(price));
                            } else if (composeType == 2) {
                                BigDecimal discountPrice = composeDTO.getPromote().multiply(new BigDecimal(lastNumber));
                                discountDescDTO.setDiscountTotal(discountPrice);
                            } else {
                                BigDecimal discountPrice = BigDecimal.ZERO;
                                for (CartSku sku : skuList) {
                                    if (tmpUsedSkuIdList.contains(sku.getSkuId())) {
                                        discountPrice = discountPrice.add(sku.getPrice().multiply(new BigDecimal(lastNumber)).multiply(BigDecimal.TEN.subtract(composeDTO.getPromote())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                                    }
                                }
                                discountDescDTO.setDiscountTotal(discountPrice);
                            }
                            composeDiscountMap.put(composeId, discountDescDTO);

                            skuList = skuList.stream().filter(tmpSku -> tmpUsedSkuIdList.contains(tmpSku.getSkuId())).collect(Collectors.toList());
                        }
                    }
                }
            }
        }
    }

    /**
     * 匹配场景营销
     *
     * @param shopId
     * @param user
     * @param skuList
     * @param usedSkuIdSet
     * @param shopIdSceneDiscountMap
     * @param sceneMarketFreeShipingMap
     */
    private void matchSceneMarket(Long shopId, CereBuyerUser user, List<CartSku> skuList, Set<Long> usedSkuIdSet, Map<Long, DiscountDescDTO> shopIdSceneDiscountMap, Map<Long, Boolean> sceneMarketFreeShipingMap) {
        BigDecimal sceneDiscount = null;
        //Distribution sceneDistribution = null;
        Long matchedSceneId = null;
        if (CollectionUtils.isNotEmpty(skuList)) {
            List<CereShopScene> sceneList = cereShopSceneService.selectOnGoingMarketingByShopId(shopId);
            if (CollectionUtils.isNotEmpty(sceneList)) {
                for (CereShopScene scene : sceneList) {
                    Calendar cal = Calendar.getInstance();
                    boolean matched = matchScene(scene, user, cal);
                    // 满足场景营销的条件
                    if (matched) {
                        CereShopSceneMember sceneMember = cereShopSceneMemberService.selectSceneMemberList(scene.getSceneId(), user.getMemberLevelId());
                        if (sceneMember != null) {
                            if (IntegerEnum.YES.getCode().equals(sceneMember.getIfFreeShipping())) {
                                //sceneDistribution = new Distribution();
                                //sceneDistribution.setDistributionName("场景营销-包邮");
                                //sceneDistribution.setDistributionPrice(BigDecimal.ZERO);
                                //sceneDistribution.setLogisticsId(-1L);
                                sceneMarketFreeShipingMap.put(shopId, true);
                            }
                            //场景营销打折
                            sceneDiscount = sceneMember.getDiscount();
                            matchedSceneId = scene.getSceneId();
                            break;
                        }
                    }
                }
            }

            if (sceneDiscount != null) {
                BigDecimal sceneTotalPrice = BigDecimal.ZERO;
                DiscountDescDTO discountDescDTO = new DiscountDescDTO();
                discountDescDTO.setDiscount(sceneDiscount);
                for (CartSku sku : skuList) {
                    sceneTotalPrice = sceneTotalPrice.add(sku.getPrice().multiply(new BigDecimal(sku.getNumber())));
                    usedSkuIdSet.add(sku.getSkuId());
                    sku.setSceneId(matchedSceneId);
                    sku.setPrice(sku.getPrice().multiply(sceneDiscount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                    discountDescDTO.setDiscountNum(discountDescDTO.getDiscountNum() + sku.getNumber());
                }
                BigDecimal sceneDiscountPrice = sceneTotalPrice.multiply(BigDecimal.TEN.subtract(sceneDiscount)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                discountDescDTO.setDiscountTotal(sceneDiscountPrice);
                shopIdSceneDiscountMap.put(shopId, discountDescDTO);
            }
            /*if (sceneDiscount != null) {
                for (CartSku sku:skuList) {
                    usedSkuIdSet.add(sku.getSkuId());
                    sku.setSceneId(matchedSceneId);
                    sku.setPrice(sku.getPrice().multiply(sceneDiscount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                }
            }*/
        }
    }

    /**
     * 匹配会员价
     *
     * @param shopId
     * @param user
     * @param skuList
     * @param usedSkuIdList
     * @param memberDiscountPriceMap
     */
    private void matchMember(Long shopId, CereBuyerUser user, List<CartSku> skuList, Set<Long> usedSkuIdList, Map<Long, DiscountDescDTO> memberDiscountPriceMap) {
        if (CollectionUtils.isNotEmpty(skuList)) {
            //BigDecimal total = BigDecimal.ZERO;
            //DiscountDescDTO discountDescDTO = new DiscountDescDTO();
            for (CartSku sku : skuList) {
                CereProductMember productMember = cereProductMemberService.selectProductMember(user.getMemberLevelId(), sku.getProductId(), sku.getSkuId());
                if (productMember != null) {
                    usedSkuIdList.add(sku.getSkuId());
                    sku.setUseMember(true);
                    //BigDecimal num = new BigDecimal(sku.getNumber());
                    if (IntegerEnum.MEMBER_PRODUCT_MODE_PRICE.getCode().equals(productMember.getMode())) {
                        sku.setPrice(productMember.getPrice());
                        //sku的价格必须 > 会员价
                        /*if (sku.getPrice().compareTo(productMember.getPrice()) > 0) {
                            total = total.add(sku.getPrice().subtract(productMember.getPrice()).multiply(num));
                        }*/
                    } else {
                        sku.setPrice(sku.getPrice().multiply(productMember.getPrice()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                        // 打折默认折扣都是正常的 价格 x 数量 x (10-折扣) / 10 就是优惠价
                        /*total = total.add(sku.getPrice().multiply(num).multiply(BigDecimal.TEN.subtract(productMember.getPrice())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));*/
                    }
                    //discountDescDTO.setDiscountNum(discountDescDTO.getDiscountNum() + sku.getNumber());
                }
            }
            /*if (!total.equals(BigDecimal.ZERO)) {
                discountDescDTO.setDiscountTotal(total);
                memberDiscountPriceMap.put(shopId, discountDescDTO);
            }*/
        }
    }

    /**
     * 匹配会员价 - 3.1.1版本
     *
     * @param skuList
     * @param usedSkuIdList
     */
    private void matchMemberForRealInfo(List<CartSku> skuList, Set<Long> usedSkuIdList, Map<Long, CartSku> cartSkuMap) {
        if (CollectionUtils.isNotEmpty(skuList)) {
            for (CartSku sku : skuList) {
                CartSku realInfo = cartSkuMap.get(sku.getSkuId());
                if (IntegerEnum.ACTIVITY_TYPE_MEMBER.getCode().equals(sku.getActivityType()) && realInfo != null) {
                    usedSkuIdList.add(sku.getSkuId());
                    sku.setUseMember(true);
                    sku.setOriginalPrice(realInfo.getOriginalPrice());
                    sku.setPrice(realInfo.getPrice());
                }
            }
        }
    }

    private void setSettlementCoupons(SettlementParam param, ShopProductParam shopProductParam, Long buyerUserId, CartSku sku,
                                      SettlementShop settlementShop, Settlement settlement,
                                      List<ProductCoupon> coupons, List<ProductCoupon> shopCoupons,
                                      Map<Long, List<Long>> productIdPlatformCouponIdMap, Map<Long, List<Long>> productIdShopCouponIdMap) {
        if (EmptyUtils.isEmpty(coupons)) {
            coupons = new ArrayList<>();
        }
        if (EmptyUtils.isEmpty(shopCoupons)) {
            shopCoupons = new ArrayList<>();
        }
        boolean flag = true;
        if (!EmptyUtils.isLongEmpty(param.getShopDiscountId()) || !EmptyUtils.isLongEmpty(param.getShopSeckillId())
                || !EmptyUtils.isLongEmpty(shopProductParam.getPlatformDiscountId()) || !EmptyUtils.isLongEmpty(shopProductParam.getPlatformSeckillId())) {
            //营销活动购买
            if (!EmptyUtils.isLongEmpty(param.getShopDiscountId())) {
                //限时折扣活动购买,查询限时折扣活动数据
                CereShopDiscount cereShopDiscount = cereShopDiscountService.findById(param.getShopDiscountId());
                if (IntegerEnum.NO.getCode().equals(cereShopDiscount.getIfAdd())) {
                    //不允许优惠叠加
                    flag = false;
                }
            } else if (!EmptyUtils.isLongEmpty(param.getShopSeckillId())) {
                //秒杀活动购买,查询秒杀活动数据
                CereShopSeckill cereShopSeckill = cereShopSeckillService.findById(param.getShopSeckillId());
                if (IntegerEnum.NO.getCode().equals(cereShopSeckill.getIfAdd())) {
                    //允许优惠叠加
                    flag = false;
                }
            } else if (!EmptyUtils.isLongEmpty(shopProductParam.getPlatformDiscountId())) {
                //平台限时折扣活动购买
                CerePlatformDiscount cerePlatformDiscount = cerePlatformDiscountService.findById(shopProductParam.getPlatformDiscountId());
                if (IntegerEnum.NO.getCode().equals(cerePlatformDiscount.getIfAdd())) {
                    //允许优惠叠加
                    flag = false;
                }
            } else if (!EmptyUtils.isLongEmpty(shopProductParam.getPlatformSeckillId())) {
                //平台秒杀活动购买
                CerePlatformSeckill cerePlatformSeckill = cerePlatformSeckillService.findById(shopProductParam.getPlatformSeckillId());
                if (IntegerEnum.NO.getCode().equals(cerePlatformSeckill.getIfAdd())) {
                    //允许优惠叠加
                    flag = false;
                }
            }
        }
        if (flag) {
            //根据商品id、商品价格查询满足条件的平台优惠券
            List<ProductCoupon> productCoupons = cereBuyerCouponService.findCouponByProduct(sku.getTotal(),
                    buyerUserId, sku.getProductId());
            //拼接活动id、满减金额字符串以便去重
            if (!EmptyUtils.isEmpty(productCoupons)) {
                List<ProductCoupon> finalCoupons = coupons;
                productCoupons.forEach(coupon -> {
                    coupon.setDistinct(coupon.getActivityId() + "-" + coupon.getFullMoney());
                    finalCoupons.add(coupon);
                });
            }
            if (!EmptyUtils.isEmpty(coupons)) {
                //去除重复活动和对应金额优惠券
                coupons = coupons.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(ProductCoupon::getDistinct))), ArrayList::new)
                );
                //记录定价捆绑商品和优惠券id的对应关系，方便后续做排除
                if (sku.getPriceId() != null && sku.getPriceId() > 0) {
                    productIdPlatformCouponIdMap.put(sku.getProductId(), coupons.stream().map(ProductCoupon::getCouponId).collect(Collectors.toList()));
                }
                if (!EmptyUtils.isEmpty(coupons)) {
                    //设置优惠券内容
                    coupons.forEach(tool -> {
                        if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(tool.getCouponType())) {
                            tool.setContent("满" + tool.getFullMoney().stripTrailingZeros().toPlainString() +
                                    "减" + tool.getReduceMoney().stripTrailingZeros().toPlainString() + "元");
                        } else {
                            tool.setContent(tool.getReduceMoney().stripTrailingZeros().toPlainString() + "折券");
                        }
                    });
                }
                settlement.setCoupons(coupons);
            }
            //查询店铺优惠券数据
            List<ProductCoupon> couponList = cereBuyerShopCouponService.findCouponByProduct(sku.getTotal(),
                    buyerUserId, sku.getProductId());
            if (!EmptyUtils.isEmpty(couponList)) {
                List<ProductCoupon> finalShopCoupons = shopCoupons;
                couponList.forEach(coupon -> {
                    coupon.setIds(cereBuyerShopCouponService.findProductIds(coupon.getShopCouponId()));
                    if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(coupon.getCouponType())) {
                        coupon.setContent("满" + coupon.getFullMoney().stripTrailingZeros().toPlainString() +
                                "减" + coupon.getReduceMoney().stripTrailingZeros().toPlainString() + "元");
                    } else {
                        coupon.setContent(coupon.getReduceMoney().stripTrailingZeros().toPlainString() + "折券");
                    }
                    finalShopCoupons.add(coupon);
                });
                //去除重复店铺优惠券
                shopCoupons = shopCoupons.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(ProductCoupon::getShopCouponId))), ArrayList::new));
                //记录定价捆绑商品和优惠券id的对应关系，方便后续做排除
                if (sku.getPriceId() != null && sku.getPriceId() > 0) {
                    productIdShopCouponIdMap.put(sku.getProductId(), shopCoupons.stream().map(ProductCoupon::getShopCouponId).collect(Collectors.toList()));
                }
            }
            settlementShop.setShopCoupons(shopCoupons);
        }
    }

    /**
     * 该方法传进来的参数，只有一个店铺，以及该店铺下购买的商品
     *
     * @param priceCombineParam
     * @param cartSkuList
     * @return
     */
    private boolean matchCombineExt(PriceCombineParam priceCombineParam, List<CartSku> cartSkuList, SettlementShop settlementShop, Map<Long, BigDecimal> shopDiscountMap) {
        boolean matchCombine = false;
        Map<Long, CartSku> map = new HashMap<>();
        for (CartSku sku : cartSkuList) {
            map.put(sku.getSkuId(), sku);
        }
        //这个价格代表，享受定价捆绑的sku的价格之和，例如：某个sku买5件，享受的是任选3件定价N元，则该值为sku的原价 * 3
        BigDecimal useToDiscountPrice = BigDecimal.ZERO;
        //享受的捆绑的定价 * 享受的次数 例如: 某个sku买10件，享受的是任选3件定价10元，则可享受3次，总共 3 * 10 = 30
        BigDecimal finalPrice = BigDecimal.ZERO;
        if (priceCombineParam != null && MapUtils.isNotEmpty(priceCombineParam.getPriceMap()) && MapUtils.isNotEmpty(priceCombineParam.getPriceProductIdListMap())) {
            Map<Long, Map<Long, List<CerePriceRule>>> priceRuleMap = priceCombineParam.getPriceMap();
            Map<Long, List<Long>> priceIdProductIdListMap = priceCombineParam.getPriceProductIdListMap();

            List<CartSku> skuList = new ArrayList<>();
            for (Long skuId : map.keySet()) {
                skuList.add(map.get(skuId));
            }
            skuList.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
            Map<Long, List<CartSku>> shopIdSkuMap = skuList.stream().collect(Collectors.groupingBy(CartSku::getShopId));
            for (Long shopId : shopIdSkuMap.keySet()) {
                List<CartSku> shopCartSkuList = shopIdSkuMap.get(shopId);
                Map<Long, List<CerePriceRule>> priceIdRuleListMap = priceRuleMap.get(shopId);
                for (Long priceId : priceIdRuleListMap.keySet()) {
                    List<Long> productIdList = priceIdProductIdListMap.get(priceId);
                    if (CollectionUtils.isEmpty(productIdList)) {
                        continue;
                    }
                    //该活动对应的等级列表，这个列表已经按照任选X件的 X做了降序排列
                    List<CerePriceRule> ruleList = priceIdRuleListMap.get(priceId);
                    for (CerePriceRule priceRule : ruleList) {
                        Integer number = priceRule.getNumber();
                        BigDecimal price = priceRule.getPrice();
                        //先筛选在活动指定商品范围中的sku
                        List<CartSku> validSkuList = new ArrayList<>();
                        Integer validBuyNum = 0;
                        for (CartSku sku : shopCartSkuList) {
                            if (productIdList.contains(sku.getProductId())) {
                                validSkuList.add(sku);
                                validBuyNum += sku.getNumber();
                            }
                        }

                        if (validBuyNum >= number) {
                            //可以优惠multiple次
                            int multiple = validBuyNum / number;
                            int remainNum = number * multiple;
                            Iterator<CartSku> ite = validSkuList.iterator();
                            while (ite.hasNext()) {
                                CartSku sku = ite.next();
                                if (remainNum >= sku.getNumber()) {
                                    useToDiscountPrice = useToDiscountPrice.add(new BigDecimal(sku.getNumber()).multiply(sku.getPrice()));
                                    remainNum = remainNum - sku.getNumber();
                                } else {
                                    useToDiscountPrice = useToDiscountPrice.add(new BigDecimal(remainNum).multiply(sku.getPrice()));
                                    remainNum = 0;
                                }
                                sku.setPriceId(priceId);
                                ite.remove();
                                if (remainNum == 0) {
                                    matchCombine = true;
                                    break;
                                }
                            }
                            finalPrice = new BigDecimal(multiple).multiply(price);
                            break;
                        }
                    }
                }
            }
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Long skuId : map.keySet()) {
            total = total.add(map.get(skuId).getPrice().multiply(new BigDecimal(map.get(skuId).getNumber())));
        }
        //设置本次定价捆绑优惠了多少钱
        shopDiscountMap.put(settlementShop.getShopId(), shopDiscountMap.get(settlementShop.getShopId()).add(useToDiscountPrice.subtract(finalPrice).setScale(2, BigDecimal.ROUND_HALF_UP)));
        return matchCombine;
    }

    /**
     * 该方法传进来的参数，只有一个店铺，以及该店铺下购买的商品
     *
     * @param priceCombineParam
     * @param cartSkuList
     * @return
     */
    private boolean matchCombineExtNew(PriceCombineParam priceCombineParam, List<CartSku> originalSkus, List<CartSku> cartSkuList, SettlementShop settlementShop, Set<Long> usedIdList, Map<Long, DiscountDescDTO> priceDiscontMap) {
        boolean matchCombine = false;
        Map<Long, CartSku> map = new HashMap<>();
        for (CartSku sku : cartSkuList) {
            map.put(sku.getSkuId(), sku);
        }
        //这个价格代表，享受定价捆绑的sku的价格之和，例如：某个sku买5件，享受的是任选3件定价N元，则该值为sku的原价 * 3
        BigDecimal useToDiscountPrice = BigDecimal.ZERO;
        //享受的捆绑的定价 * 享受的次数 例如: 某个sku买10件，享受的是任选3件定价10元，则可享受3次，总共 3 * 10 = 30
        BigDecimal finalPrice = BigDecimal.ZERO;
        Long usedPriceId = null;
        BigDecimal composePrice = null;
        Integer composeNum = null;
        Integer discountNum = null;
        if (priceCombineParam != null && MapUtils.isNotEmpty(priceCombineParam.getPriceMap()) && MapUtils.isNotEmpty(priceCombineParam.getPriceProductIdListMap())) {
            Map<Long, Map<Long, List<CerePriceRule>>> priceRuleMap = priceCombineParam.getPriceMap();
            Map<Long, List<Long>> priceIdProductIdListMap = priceCombineParam.getPriceProductIdListMap();

            List<CartSku> skuList = new ArrayList<>();
            for (Long skuId : map.keySet()) {
                skuList.add(map.get(skuId));
            }
            skuList.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
            Map<Long, List<CartSku>> shopIdSkuMap = skuList.stream().collect(Collectors.groupingBy(CartSku::getShopId));
            for (Long shopId : shopIdSkuMap.keySet()) {
                List<CartSku> shopCartSkuList = shopIdSkuMap.get(shopId);
                Map<Long, List<CerePriceRule>> priceIdRuleListMap = priceRuleMap.get(shopId);
                for (Long priceId : priceIdRuleListMap.keySet()) {
                    List<Long> productIdList = priceIdProductIdListMap.get(priceId);
                    if (CollectionUtils.isEmpty(productIdList)) {
                        continue;
                    }
                    //该活动对应的等级列表，这个列表已经按照任选X件的 X做了降序排列
                    List<CerePriceRule> ruleList = priceIdRuleListMap.get(priceId);
                    for (CerePriceRule priceRule : ruleList) {
                        Integer number = priceRule.getNumber();
                        BigDecimal price = priceRule.getPrice();
                        //先筛选在活动指定商品范围中的sku
                        List<CartSku> validSkuList = new ArrayList<>();
                        Integer validBuyNum = 0;
                        for (CartSku sku : shopCartSkuList) {
                            if (productIdList.contains(sku.getProductId())) {
                                validSkuList.add(sku);
                                validBuyNum += sku.getNumber();
                            }
                        }

                        if (validBuyNum >= number) {
                            //可以优惠multiple次
                            int multiple = validBuyNum / number;
                            int remainNum = number * multiple;
                            Iterator<CartSku> ite = validSkuList.iterator();
                            while (ite.hasNext()) {
                                CartSku sku = ite.next();
                                if (remainNum >= sku.getNumber()) {
                                    useToDiscountPrice = useToDiscountPrice.add(new BigDecimal(sku.getNumber()).multiply(sku.getPrice()));
                                    remainNum = remainNum - sku.getNumber();
                                } else {
                                    useToDiscountPrice = useToDiscountPrice.add(new BigDecimal(remainNum).multiply(sku.getPrice()));
                                    remainNum = 0;
                                }
                                sku.setPriceId(priceId);
                                usedIdList.add(sku.getSkuId());
                                ite.remove();
                                if (remainNum == 0) {
                                    matchCombine = true;
                                    usedPriceId = priceId;
                                    composePrice = price;
                                    composeNum = number;
                                    discountNum = multiple;
                                    break;
                                }
                            }
                            finalPrice = new BigDecimal(multiple).multiply(price);
                            break;
                        }
                    }
                }
            }
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Long skuId : map.keySet()) {
            total = total.add(map.get(skuId).getPrice().multiply(new BigDecimal(map.get(skuId).getNumber())));
        }
        //设置本次定价捆绑优惠了多少钱
        if (matchCombine) {
            DiscountDescDTO discountDescDTO = new DiscountDescDTO();
            discountDescDTO.setComposePrice(composePrice);
            discountDescDTO.setComposeNum(composeNum);
            discountDescDTO.setDiscountNum(discountNum);
            discountDescDTO.setDiscountTotal(useToDiscountPrice.subtract(finalPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            priceDiscontMap.put(usedPriceId, discountDescDTO);
        }
        return matchCombine;
    }

    private boolean matchScene(CereShopScene scene, CereBuyerUser user, Calendar cal) {
        boolean matched = false;
        if (IntegerEnum.SCENE_TYPE_FESTIVAL.getCode().equals(scene.getSceneType())) {
            String startTime = scene.getStartTime();
            String endTime = scene.getEndTime();
            String nowTime = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
            if (startTime.substring(0, 10).compareTo(nowTime) <= 0 && nowTime.compareTo(endTime.substring(0, 10)) <= 0) {
                matched = true;
            }
        } else if (IntegerEnum.SCENE_TYPE_MEMBER.getCode().equals(scene.getSceneType())) {
            String[] timeArr = scene.getSceneTime().split("-");
            if (IntegerEnum.SCENE_TIME_MONTH.getCode().equals(scene.getSceneTimeType())) {
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                if (Integer.parseInt(timeArr[0]) <= dayOfMonth && dayOfMonth <= Integer.parseInt(timeArr[1])) {
                    matched = true;
                }
            } else if (IntegerEnum.SCENE_TIME_WEEK.getCode().equals(scene.getSceneTimeType())) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                List<Integer> timeList = new ArrayList<>();
                for (String timeStr : timeArr) {
                    if (timeStr.equals("1")) {
                        timeList.add(7);
                    } else {
                        timeList.add(Integer.parseInt(timeStr) - 1);
                    }
                }
                if (timeList.contains(dayOfWeek)) {
                    matched = true;
                }
            } else {
                String nowTime = DateUtil.format(cal.getTime(), "HH:mm:ss");
                if (timeArr[0].compareTo(nowTime) <= 0 && nowTime.compareTo(timeArr[1]) <= 0) {
                    matched = true;
                }
            }
        } else {
            String birthday = user.getBirthday();
            if (StringUtils.isBlank(birthday)) {
                return false;
            }
            if (IntegerEnum.SCENE_TIME_BIRTHDAY.getCode().equals(scene.getSceneTimeType())) {
                if (DateUtil.format(cal.getTime(), "yyyy-MM-dd").equals(birthday)) {
                    matched = true;
                }
            } else if (IntegerEnum.SCENE_TIME_BIRTHDAY_WEEK.getCode().equals(scene.getSceneTimeType())) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 1) {
                    dayOfWeek = 8;
                }
                cal.add(Calendar.DATE, -1 * (dayOfWeek - 2));
                // 当前时间对应的周的周一
                String startDateStr = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
                cal.add(Calendar.DATE, 6);
                // 当前时间对应的周的周日
                String endDateStr = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
                if (birthday.compareTo(startDateStr) >= 0 && birthday.compareTo(endDateStr) <= 0) {
                    matched = true;
                }
            } else {
                if (birthday.substring(0, 7).equals(DateUtil.format(cal.getTime(), "yyyy-MM"))) {
                    matched = true;
                }
            }
        }
        return matched;
    }

    /**
     * 结算接口校验
     */
    private void checkActivity(SettlementParam param, Integer number, CartSku sku, Settlement settlement,
                               CereBuyerUser user, ShopProductParam shop) throws CoBusinessException {
        List<ProductCoupon> coupons = null;
        Long skuId = sku.getSkuId();
        if (!EmptyUtils.isEmpty(param.getShopSeckillId()) || !EmptyUtils.isEmpty(shop.getShopSeckillId())) {
            Long shopSeckillId = param.getShopSeckillId();
            if (EmptyUtils.isEmpty(shopSeckillId)) {
                shopSeckillId = shop.getShopSeckillId();
            }
            //查询秒杀活动数据
            CereShopSeckill cereShopSeckill = cereShopSeckillService.findById(shopSeckillId);
            if (cereShopSeckill != null) {
                if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopSeckill.getIfLimit())) {
                    //如果秒杀活动限购,校验数量
                    if (number > cereShopSeckill.getLimitNumber()) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
            }
        }
        if (!EmptyUtils.isEmpty(param.getShopDiscountId()) || !EmptyUtils.isEmpty(shop.getShopDiscountId())) {
            Long shopDiscountId = param.getShopDiscountId();
            if (EmptyUtils.isEmpty(shopDiscountId)) {
                shopDiscountId = shop.getShopDiscountId();
            }
            //查询限时折扣活动数据
            CereShopDiscount cereShopDiscount = cereShopDiscountService.findById(shopDiscountId);
            if (cereShopDiscount != null) {
                if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopDiscount.getIfLimit())) {
                    //如果限时折扣活动限购,校验数量
                    if (number > cereShopDiscount.getLimitNumber()) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
            }
        }
        if (!EmptyUtils.isEmpty(shop.getPlatformSeckillId())) {
            Long platformSeckillId = shop.getPlatformSeckillId();
            //查询秒杀活动数据
            CerePlatformSeckill cerePlatformSeckill = cerePlatformSeckillService.findById(platformSeckillId);
            if (cerePlatformSeckill != null && IntegerEnum.ACTIVITY_START.getCode().equals(cerePlatformSeckill.getState())) {
                if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cerePlatformSeckill.getIfLimit())) {
                    //如果秒杀活动限购,校验数量
                    if (number > cerePlatformSeckill.getLimitNumber()) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
            }
        }
        if (!EmptyUtils.isEmpty(shop.getPlatformDiscountId())) {
            Long platformDiscountId = shop.getPlatformDiscountId();
            //查询限时折扣活动数据
            CerePlatformDiscount cerePlatformDiscount = cerePlatformDiscountService.findById(platformDiscountId);
            if (cerePlatformDiscount != null && IntegerEnum.ACTIVITY_START.getCode().equals(cerePlatformDiscount.getState())) {
                if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cerePlatformDiscount.getIfLimit())) {
                    //如果限时折扣活动限购,校验数量
                    if (number > cerePlatformDiscount.getLimitNumber()) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
            }
        }
    }

    /**
     * 提交订单校验秒杀活动限量和限购
     */
    private void checkSeckillActivity(CereShopSeckill cereShopSeckill, Integer number, CartSku sku, List<CereShopSeckillDetail> seckillDetails) throws CoBusinessException {
        //定义加锁key
        String key = "秒杀活动商品仅剩件数" + cereShopSeckill.getShopSeckillId();
        //加锁
        RLock redissonLock = redissonClient.getLock(key);
        redissonLock.lock();
        if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopSeckill.getIfLimit())) {
            //如果秒杀活动限购,校验数量
            if (number > cereShopSeckill.getLimitNumber()) {
                throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR, redissonLock);
            }
        }
        if (IntegerEnum.YES.getCode().equals(cereShopSeckill.getIfNumber())) {
            int surplusNumber = 0;
            ProductStockInfo productStockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode(),
                    sku.getSkuId());
            if (productStockInfo != null) {
                surplusNumber = productStockInfo.getStockNumber();
            } else {
                surplusNumber = cereShopSeckillDetailService.findNumber(cereShopSeckill.getShopSeckillId(), sku.getSkuId());
            }
            //如果限量,查询缓存中活动商品仅剩数量
            /*if (EmptyUtils.isEmpty(stringRedisService.get("秒杀活动商品仅剩件数" + cereShopSeckill.getShopSeckillId() + sku.getSkuId()))) {
                //如果缓存中没有,取数据库中仅剩件数
                surplusNumber=cereShopSeckillDetailService.findNumber(cereShopSeckill.getShopSeckillId(),sku.getSkuId());
            } else {
                //如果缓存中有,就使用缓存中仅剩件数
                surplusNumber = (int) stringRedisService.get("秒杀活动商品仅剩件数" + cereShopSeckill.getShopSeckillId() + sku.getSkuId());
            }*/
            //判断数量是否大于当前购买数量
            if (number > surplusNumber) {
                throw new CoBusinessException(CoReturnFormat.PRODUCT_ALREADY_SELL_OUT, redissonLock);
            }
            //设置更新限量库存数据
            CereShopSeckillDetail detail = new CereShopSeckillDetail();
            detail.setShopSeckillId(cereShopSeckill.getShopSeckillId());
            detail.setSkuId(sku.getSkuId());
            detail.setNumber(surplusNumber - number);
            seckillDetails.add(detail);
        }
        //解锁
        redissonLock.unlock();
    }

    /**
     * 提交订单校验限时折扣限量和限购
     */
    private void checkDiscountActivity(CereShopDiscount cereShopDiscount, Integer number, CartSku sku, List<CereShopDiscountDetail> discountDetails) throws CoBusinessException {
        //定义加锁key
        String key = "限时折扣活动商品仅剩件数" + cereShopDiscount.getShopDiscountId();
        //加锁
        RLock redissonLock = redissonClient.getLock(key);
        redissonLock.lock();
        if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopDiscount.getIfLimit())) {
            //如果限时折扣活动限购,校验数量
            if (number > cereShopDiscount.getLimitNumber()) {
                throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR, redissonLock);
            }
        }
        if (IntegerEnum.YES.getCode().equals(cereShopDiscount.getIfNumber())) {
            int surplusNumber = 0;
            ProductStockInfo productStockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode(),
                    sku.getSkuId());
            if (productStockInfo != null) {
                surplusNumber = productStockInfo.getStockNumber();
            } else {
                surplusNumber = cereShopDiscountDetailService.findNumber(cereShopDiscount.getShopDiscountId(), sku.getSkuId());
            }
            //如果限量,查询缓存中活动商品仅剩数量
            /*if(EmptyUtils.isEmpty(stringRedisService.get("限时折扣活动商品仅剩件数"+cereShopDiscount.getShopDiscountId()+sku.getSkuId()))){
                //如果没有,取数据库仅剩件数
                surplusNumber=cereShopDiscountDetailService.findNumber(cereShopDiscount.getShopDiscountId(),sku.getSkuId());
            }else {
                //如果有,取缓存
                surplusNumber= (int) stringRedisService.get("限时折扣活动商品仅剩件数"+cereShopDiscount.getShopDiscountId()+sku.getSkuId());
            }*/
            //判断数量是否大于当前购买数量
            if (number > surplusNumber) {
                throw new CoBusinessException(CoReturnFormat.PRODUCT_ALREADY_SELL_OUT, redissonLock);
            }
            //设置更新限量库存数据
            CereShopDiscountDetail detail = new CereShopDiscountDetail();
            detail.setShopDiscountId(cereShopDiscount.getShopDiscountId());
            detail.setSkuId(sku.getSkuId());
            detail.setNumber(surplusNumber - number);
            discountDetails.add(detail);
        }
        //解锁
        redissonLock.unlock();
    }

    @Override
    public Distribution setLogisticPrice(List<CereOrderLogistics> logistics, List<CartSku> skus, CereBuyerReceive receive,
                                         Map<Long, Integer> buyNumberMap) throws CoBusinessException {
        if (receive == null) {
            return null;
        }
        Distribution tribution = new Distribution();
        tribution.setDistributionPrice(BigDecimal.ZERO);
        //过滤不需要物流的商品
        List<CartSku> cartSkus = skus.stream().filter(sku -> IntegerEnum.YES.getCode().equals(sku.getIfLogistics()))
                .collect(Collectors.toList());
        if (EmptyUtils.isEmpty(cartSkus)) {
            tribution.setDistributionName("全国包邮");
            return tribution;
        }
        return DistributionUtil.getDistribution(logistics, receive, buyNumberMap, cartSkus);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void pay(PayParam param) throws CoBusinessException, Exception {
        String time = TimeUtils.yyMMddHHmmss();
        List<CereShopOrder> list = null;
        if (ParamEnum.ORDER_TYPE_PARENT.getCode().equals(param.getType())) {
            //父订单支付,查询所有子订单数据
            list = cereShopOrderDAO.findByParentId(param.getOrderId());
        } else {
            //子订单支付
            list = cereShopOrderDAO.findByOrderId(param.getOrderId());
        }
        //支付业务处理
        if (!EmptyUtils.isEmpty(list)) {
            List<CereDistributionOrder> distributionOrders = new ArrayList<>();
            List<CereShopConversion> conversions = new ArrayList<>();
            for (CereShopOrder order : list) {
                if (!EmptyUtils.isEmpty(order.getShopGroupWorkId())) {
                    //拼单支付业务处理
                    handleGroupWork(order, RandomStringUtil.getRandomCode(15, 0),
                            order.getOrderFormid() + "-" + RandomStringUtil.getRandomCode(4, 0) + "XCX", time);
                } else if (!EmptyUtils.isEmpty(order.getShopSeckillId())) {
                    //秒杀活动支付业务处理
                    handleSeckill(order, RandomStringUtil.getRandomCode(15, 0),
                            order.getOrderFormid() + "-" + RandomStringUtil.getRandomCode(4, 0) + "XCX", time);
                } else if (!EmptyUtils.isEmpty(order.getShopDiscountId())) {
                    //限时折扣活动支付业务处理
                    handleDiscount(order, RandomStringUtil.getRandomCode(15, 0),
                            order.getOrderFormid() + "-" + RandomStringUtil.getRandomCode(4, 0) + "XCX", time);
                } else {
                    //正常下单业务处理
                    handleOrder(order, RandomStringUtil.getRandomCode(15, 0),
                            order.getOrderFormid() + "-" + RandomStringUtil.getRandomCode(4, 0) + "XCX", time, distributionOrders, conversions);
                }
            }
            if (!EmptyUtils.isEmpty(distributionOrders)) {
                //批量插入分销订单数据
                cereDistributionOrderService.insertBatch(distributionOrders);
            }
            if (!EmptyUtils.isEmpty(conversions)) {
                //批量插入转化数据
                cereShopConversionService.insertBatch(conversions);
            }
        }
    }

    @Override
    public PayUrl getUrl(OrderGetByIdParam param, CereBuyerUser user, String ip) throws CoBusinessException, Exception {
        if (param.getType() == null) {
            param.setType(2);
        }
        PayUrl payUrl = new PayUrl();
        //根据订单id查询支付金额数据
        String orderFormId = null;
        BigDecimal price = null;
        CereOrderParent orderParent = param.getType() == 1 ? cereOrderParentDAO.findById(param.getOrderId()) : null;
        if (orderParent != null) {
            orderFormId = orderParent.getParentFormid();
            price = orderParent.getPrice();
        } else {
            CereShopOrder shopOrder = cereShopOrderDAO.findById(param.getOrderId());
            if (shopOrder != null) {
                orderFormId = shopOrder.getOrderFormid();
                price = shopOrder.getPrice();
            }
        }
        if (orderFormId != null && price != null) {
            payUrl.setOrderId(param.getOrderId());
            payUrl.setMoney(price);
            //生成收款码
            String formid = orderFormId + "-" + RandomStringUtil.getRandomCode(3, 0) + "XCX";
            String code_url = wxPayService.getOrderCollectionCode(formid, price,
                    ip, WxPayEnum.PAY_TYPE_NATIVE.getCode());
            if (!EmptyUtils.isEmpty(code_url)) {
                //生成收款二维码图片
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
                byte[] bytes = out.toByteArray();
                String url = uploadService.uploadByte(formid + ".png", bytes);
                payUrl.setUrl(url);
            }
        }
        return payUrl;
    }

    @Override
    public PayUrl checkPay(PayParam param) throws CoBusinessException {
        PayUrl payUrl = new PayUrl();
        List<CereShopOrder> list = null;
        //查询订单是否支付成功
        if (ParamEnum.ORDER_TYPE_PARENT.getCode().equals(param.getType())) {
            //父订单校验
            list = cereShopOrderDAO.checkPayParent(param.getOrderId());
        } else {
            //子订单校验
            list = cereShopOrderDAO.checkPayOrder(param.getOrderId());
        }
        if (!EmptyUtils.isEmpty(list)) {
            payUrl.setCode(StringEnum.WX_PAY_SUCCESS.getCode());
        }
        return payUrl;
    }

    @Override
    public List<CartSku> refund(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException {
        List<CartSku> skus = null;
        //查询当前订单已经提交过的售后商品规格id数组
        List<Long> ids = cereShopOrderDAO.findAfterSkuIdsByOrderId(param.getOrderId());
        if (EmptyUtils.isEmpty(ids)) {
            //未申请过售后或者售后失败等,查询订单所有商品列表返回
            skus = cereProductSkuService.findSkuByOrderId(param.getOrderId());
        } else {
            //有售后商品,查询当前订单除了这些商品以外的其他商品
            skus = cereProductSkuService.findSkuByIds(ids, param.getOrderId());
        }
        return skus;
    }

    @Override
    public void payGift(String orderFormId) {
        log.info("payGift orderFormId = {}", orderFormId);
        List<CereShopOrder> list = null;
        Long orderId = null;
        //查询是否为父订单编号
        CereOrderParent parent = cereShopOrderDAO.findByParentFormid(orderFormId);
        if (parent != null) {
            //查询所有子订单数据
            list = cereShopOrderDAO.findByParentId(parent.getParentId());
            orderId = parent.getParentId();
        } else {
            //子订单支付回调
            list = cereShopOrderDAO.findByFormid(orderFormId);
            orderId = list.get(0).getOrderId();
        }
        CerePlatformPolite polite = cerePlatformPoliteService.selectOnGoingPolite();
        log.info("payGift onGoingPolite = {}", JSON.toJSONString(polite));
        if (polite != null) {
            boolean matchCondition = false;
            //购买一定金额商品
            if (polite.getBuyerMode() == 1) {
                BigDecimal totalPrice = list.stream().map(CereShopOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                //满足条件
                if (totalPrice.compareTo(polite.getBuyer()) >= 0) {
                    matchCondition = true;
                }
            } else {//购买一定数量商品
                List<CereOrderProduct> opList = cereOrderProductService.findByOrderIds(list.stream().map(CereShopOrder::getOrderId).collect(Collectors.toList()));
                int totalBuyNum = opList.stream().mapToInt(CereOrderProduct::getNumber).sum();
                if (totalBuyNum >= polite.getBuyer().intValue()) {
                    matchCondition = true;
                }
            }
            log.info("payGift matchCondition = {}", matchCondition);
            if (matchCondition) {
                Long buyerUserId = list.get(0).getBuyerUserId();
                if (buyerUserId != null) {
                    CereBuyerUser user = cereBuyerUserService.selectByBuyerUserId(buyerUserId);
                    if (user == null) {
                        return;
                    }
                    String time = TimeUtils.yyMMddHHmmss();
                    // 送成长值
                    if (polite.getGrowth() != null && polite.getGrowth() > 0) {
                        if (user.getMemberLevelId() != null && user.getMemberLevelId() > 0) {
                            user.setGrowth(user.getGrowth() + polite.getGrowth());
                            CerePlatformMemberLevel nextLevel = cerePlatformMemberLevelService.selectNextLevel(user.getMemberLevelId());
                            if (nextLevel != null && user.getGrowth() >= nextLevel.getGrowth()) {
                                user.setMemberLevelId(nextLevel.getMemberLevelId());
                            }
                        } else {
                            CerePlatformMemberLevel level = cerePlatformMemberLevelService.selectFirstLevel();
                            if (level != null) {
                                user.setMemberLevelId(level.getMemberLevelId());
                                user.setGrowth(polite.getGrowth());
                            }
                        }
                        // 由于selectByBuyerUserId 可能更改了wechatName字段，所以这里重新设置为null，这样就不会更新到
                        user.setWechatName(null);
                        try {
                            cereBuyerUserService.update(user, null);

                            CereBuyerPoliteRecord record = new CereBuyerPoliteRecord();
                            record.setBuyerUserId(buyerUserId);
                            record.setOrderId(orderId);
                            record.setPoliteId(polite.getPoliteId());
                            record.setPoliteType(IntegerEnum.POLITE_TYPE_GROWTH.getCode());
                            record.setGrowth(polite.getGrowth());
                            record.setCreateTime(time);
                            record.setUpdateTime(time);
                            cereBuyerPoliteRecordDAO.insert(record);
                        } catch (Exception e) {
                            log.error("payGift update user failed: orderFormId = {}", orderFormId, e);
                        }
                    }

                    //送积分
                    //判断积分开关是否有打开
                    if (polite.getCredit() != null && polite.getCredit() > 0) {
                        CerePlatformDict creditSwitchDict = cerePlatformDictService.getByName("credit_switch");
                        if (creditSwitchDict.getDictDescribe() != null
                                && IntegerEnum.YES.getCode().equals(Integer.parseInt(creditSwitchDict.getDictDescribe()))) {
                            //加积分
                            cereBuyerUserService.increaseCredit(buyerUserId, polite.getCredit(), CreditOptTypeEnum.POLITE_GIFT);
                            //记录支付有礼
                            CereBuyerPoliteRecord record = new CereBuyerPoliteRecord();
                            record.setBuyerUserId(buyerUserId);
                            record.setOrderId(orderId);
                            record.setPoliteId(polite.getPoliteId());
                            record.setPoliteType(IntegerEnum.POLITE_TYPE_CREDIT.getCode());
                            record.setCredit(polite.getCredit());
                            record.setCreateTime(time);
                            record.setUpdateTime(time);
                            cereBuyerPoliteRecordDAO.insert(record);
                        }
                    }

                    //送优惠券
                    List<CerePlatformPoliteActivity> activityList = cerePlatformPoliteActivityService.selectByPoliteId(polite.getPoliteId());
                    if (CollectionUtils.isNotEmpty(activityList)) {
                        for (CerePlatformPoliteActivity activity : activityList) {
                            CouponParam couponParam = new CouponParam();
                            couponParam.setCouponId(activity.getActivityId());
                            couponParam.setSource(IntegerEnum.COUPON_SOURCE_POLITE.getCode());
                            try {
                                cereBuyerCouponService.takeCoupon(couponParam, user);

                                CereBuyerPoliteRecord record = new CereBuyerPoliteRecord();
                                record.setBuyerUserId(buyerUserId);
                                record.setOrderId(orderId);
                                record.setPoliteId(polite.getPoliteId());
                                if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(activity.getActivityType())) {
                                    record.setPoliteType(IntegerEnum.POLITE_TYPE_REDUCTION.getCode());
                                } else {
                                    record.setPoliteType(IntegerEnum.POLITE_TYPE_DISCOUNT.getCode());
                                }
                                record.setDiscount(activity.getCouponContent());
                                record.setCreateTime(time);
                                record.setUpdateTime(time);
                                cereBuyerPoliteRecordDAO.insert(record);
                            } catch (Exception e) {
                                log.error("payGift takeCoupon failed: couponId = {}", activity.getActivityId(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public OrderPoliteDTO getOrderPolite(Long buyerUserId, Long orderId) {
        List<CereBuyerPoliteRecord> recordList = cereBuyerPoliteRecordDAO.selectPoliteRecord(buyerUserId, orderId);
        OrderPoliteDTO politeDTO = new OrderPoliteDTO();
        if (CollectionUtils.isNotEmpty(recordList)) {
            List<OrderPoliteCouponDTO> politeCouponDTOList = new ArrayList<>();
            for (CereBuyerPoliteRecord record : recordList) {
                if (IntegerEnum.POLITE_TYPE_GROWTH.getCode().equals(record.getPoliteType())) {
                    politeDTO.setGrowth(record.getGrowth());
                } else if (IntegerEnum.POLITE_TYPE_CREDIT.getCode().equals(record.getPoliteType())) {
                    politeDTO.setCredit(record.getCredit());
                } else {
                    OrderPoliteCouponDTO couponDTO = new OrderPoliteCouponDTO();
                    if (IntegerEnum.POLITE_TYPE_REDUCTION.getCode().equals(record.getPoliteType())) {
                        couponDTO.setCouponType(IntegerEnum.COUPON_TYPE_REDUTION.getCode());
                    } else {
                        couponDTO.setCouponType(IntegerEnum.COUPON_TYPE_DISCOUNT.getCode());
                    }
                    couponDTO.setDiscount(record.getDiscount());
                    politeCouponDTOList.add(couponDTO);
                }
            }
            politeDTO.setCouponList(politeCouponDTOList);
            return politeDTO;
        }
        return null;
    }

    @Override
    public Integer getWaitPayNumByHours(Integer hours, Long buyerUserId) {
        Date time = DateUtil.offsetHour(new Date(), -hours);
        LambdaQueryWrapper<CereShopOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereShopOrder::getBuyerUserId, buyerUserId)
                .eq(CereShopOrder::getPaymentState, IntegerEnum.NO.getCode())
                .ge(CereShopOrder::getCreateTime, DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        return cereShopOrderDAO.selectCount(wrapper);
    }

    @Override
    public Integer getOrderNumByHours(Integer hours, Long buyerUserId) {
        Date time = DateUtil.offsetHour(new Date(), -hours);
        LambdaQueryWrapper<CereShopOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereShopOrder::getBuyerUserId, buyerUserId)
                .ge(CereShopOrder::getCreateTime, DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        return cereShopOrderDAO.selectCount(wrapper);
    }


    /**
     * 查询定价捆绑销售的活动
     *
     * @param shopIdList
     * @param productIdList
     * @return
     */
    private PriceCombineParam getPriceParam(List<Long> shopIdList, List<Long> productIdList) {
        PriceCombineParam param = new PriceCombineParam();
        Map<Long, Map<Long, List<CerePriceRule>>> map = cereShopPriceService.selectPriceMap(shopIdList);
        param.setPriceMap(map);
        List<Long> priceIdList = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<Long, Map<Long, List<CerePriceRule>>> entry : map.entrySet()) {
                Map<Long, List<CerePriceRule>> priceIdRuleListMap = entry.getValue();
                if (priceIdRuleListMap != null) {
                    for (Map.Entry<Long, List<CerePriceRule>> priceIdEntry : priceIdRuleListMap.entrySet()) {
                        priceIdList.add(priceIdEntry.getKey());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(priceIdList)) {
            Map<Long, List<Long>> priceProductIdListMap = cerePriceProductService.selectProductIdListMap(priceIdList, productIdList);
            param.setPriceProductIdListMap(priceProductIdListMap);
        }

        return param;
    }

}
