package com.shop.cereshop.app.service.order.placeOrderTemplate;

import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.app.page.order.PayUrl;
import com.shop.cereshop.app.param.order.OrderParam;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.app.pay.weixin.service.WxPayService;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.buyer.CereBuyerReceiveService;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.compose.CereShopComposeService;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderDetailService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkService;
import com.shop.cereshop.app.service.logistics.CereOrderLogisticsService;
import com.shop.cereshop.app.service.order.CereOrderParentService;
import com.shop.cereshop.app.service.order.CereOrderProductAttributeService;
import com.shop.cereshop.app.service.order.CereOrderProductService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.app.service.price.CereShopPriceService;
import com.shop.cereshop.app.service.product.CereProductMemberService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.app.service.stock.CereStockService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.ParamEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWork;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author pepis
 * @date 2021/12/9 18:37
 * @apiNote 拼团下单
 */
@Component
public class LaunchPlaceOrder extends PlaceOrderTemplate {

    @Autowired
    private CereCollageOrderService cereCollageOrderService;

    @Autowired
    private CereCollageOrderDetailService cereCollageOrderDetailService;

    public LaunchPlaceOrder(CereBuyerShopCouponService cereBuyerShopCouponService, CerePlatformSeckillService cerePlatformSeckillService,
                            CerePlatformDiscountService cerePlatformDiscountService, CereShopSeckillService cereShopSeckillService,
                            CereShopDiscountService cereShopDiscountService, CereShopPriceService cereShopPriceService, CereShopComposeService cereShopComposeService,
                            CereStockService cereStockService, CereProductSkuService cereProductSkuService, CereShopSceneMemberService cereShopSceneMemberService,
                            CereProductMemberService cereProductMemberService, CereBuyerCouponService cereBuyerCouponService, WxPayService wxPayService,
                            UploadService uploadService, CereOrderParentService cereOrderParentService, CereOrderProductService cereOrderProductService,
                            CereShopOrderDAO cereShopOrderDAO, StringRedisService stringRedisService, CereRedisKeyServcice cereRedisKeyServcice,
                            CereOrderProductAttributeService cereOrderProductAttributeService, CereBuyerReceiveService cereBuyerReceiveService,
                            CereShopCartService cereShopCartService, CereShopGroupWorkService cereShopGroupWorkService, CereBuyerUserService cereBuyerUserService,
                            CerePlatformActivityService cerePlatformActivityService, WxCardUtil wxCardUtil, CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO,
                            CereOrderLogisticsService cereOrderLogisticsService,
                            CereShopOrderService cereShopOrderService) {
        super(cereBuyerShopCouponService, cerePlatformSeckillService, cerePlatformDiscountService, cereShopSeckillService,
                cereShopDiscountService, cereShopPriceService, cereShopComposeService, cereStockService, cereProductSkuService,
                cereShopSceneMemberService, cereProductMemberService, cereBuyerCouponService, wxPayService, uploadService,
                cereOrderParentService, cereOrderProductService, cereShopOrderDAO, stringRedisService, cereRedisKeyServcice,
                cereOrderProductAttributeService, cereBuyerReceiveService, cereShopCartService, cereShopGroupWorkService,
                cereBuyerUserService, cerePlatformActivityService, wxCardUtil, cereSkuMemberRealInfoDAO,
                cereOrderLogisticsService, cereShopOrderService);
    }

    @Override
    protected void check(OrderParam param) throws CoBusinessException {
        //查询拼团活动数据
        CereShopGroupWork cereShopGroupWork = cereShopGroupWorkService.findById(param.getShopGroupWorkId());
        if (null == cereShopGroupWork || EmptyUtils.isEmpty(param.getShops())) {
            throw new CoBusinessException(CoReturnFormat.PARAM_INVALID);
        }
    }

//    @Override
//    public void placeOrder(OrderParam param, CereBuyerUser user, String ip, PayUrl payUrl, String time) throws Exception {
//        //查询拼团活动数据
//        CereShopGroupWork cereShopGroupWork = cereShopGroupWorkService.findById(param.getShopGroupWorkId());
//        if (null == cereShopGroupWork || EmptyUtils.isEmpty(param.getShops())) {
//            return;
//        }
//        //定义map封装商品购买数量
//        Map<Long, ProductSku> numberMap = getBuySkuMap(param);
//        //校验库存 本次下单中的商品是否有库存不足
//        AtomicBoolean flag = new AtomicBoolean(false);
//        //计算运费
//        BigDecimal logisticPrice = calLogisticPrice(param);
//
//        //促销活动优惠金额
//        AtomicReference<BigDecimal> skuDiscountPrice = new AtomicReference<>(BigDecimal.ZERO);
//        Map<Long, CartSku> map = getLongCartSkuMap(param, user, numberMap, flag, skuDiscountPrice);
//
//        if (flag.get()) {
//            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
//        }
//        //计算订单总金额
//        BigDecimal orderPrice = calOrderPrice(map, numberMap);
//        orderPrice = orderPrice.subtract(skuDiscountPrice.get());
//        //查询平台优惠券数据
//        CereBuyerCoupon cereBuyerCoupon = getBuyerCoupon(param.getCouponId(), user.getBuyerUserId());
//
//        //定义店铺优惠券map
//        Map<Long, CereBuyerShopCoupon> discountMap = new HashMap<>();
//
//        //计算优惠金额
//        BigDecimal discountPrice = setDiscountPrice(cereBuyerCoupon, param, map, numberMap, discountMap, orderPrice);
//        param.setDiscountPrice(discountPrice);
//
//        //计算订单实付金额=订单总金额-优惠+运费
//        BigDecimal payPrice = orderPrice.subtract(discountPrice).add(logisticPrice);
//        param.setPrice(payPrice);
//        //校验实付金额
//        if (EmptyUtils.isEmptyBigdecimal(payPrice)) {
//            //如果金额为0,提示
//            throw new CoBusinessException(CoReturnFormat.PAY_MONEY_NOT_ZERO);
//        }
//
//        //新增父订单数据
//        CereOrderParent parent = createParentOrder(payPrice, time, logisticPrice, orderPrice);
//
//        payUrl.setMoney(payPrice);
//        payUrl.setOrderId(parent.getParentId());
//
//        //生成支付二维码
//        generatePayUrl(param, user.getWechatOpenId(), parent.getParentFormid(), ip, payUrl);
//
//        //遍历店铺数据,新增订单
//        createShopOrders(parent.getParentId(), param, user, time, map, cereBuyerCoupon, discountMap, cereShopGroupWork, payUrl);
//    }

    @Override
    public Map<Long, CartSku> getLongCartSkuMap(OrderParam param, CereBuyerUser user, Map<Long, ProductSku> numberMap, AtomicBoolean flag,
                                                AtomicReference<BigDecimal> skuDiscountPrice, Map<Long, BigDecimal> shopDiscountPriceMap) {
        Map<Long, CartSku> map = new HashMap<>();

        for (OrderProductParam shop : param.getShops()) {

            if (!EmptyUtils.isEmpty(shop.getSkus())) {
                //查询当前店铺所有购买商品的库存数据
                List<CartSku> productSkus = cereProductSkuService.findGroupWorkStockNumberBySkus(shop.getSkus(), param.getShopGroupWorkId());
                if (!EmptyUtils.isEmpty(productSkus)) {
                    productSkus.forEach(sku -> {
                        //设置购买数量
                        sku.setNumber(numberMap.get(sku.getSkuId()).getNumber());
                        //设置选中状态
                        //sku.setSelected(numberMap.get(sku.getSkuId()).getSelected());
                        //设置店铺id
                        sku.setShopId(shop.getShopId());
                        map.put(sku.getSkuId(), sku);
                    });
                }
                shop.getSkus().forEach(sku -> {
                    if (IntegerEnum.NO.getCode().equals(map.get(sku.getSkuId()).getIfOversold())) {
                        //如果不允许超卖,校验redis中商品库存
                        int stockNumber = 0;
                                /*if(!EmptyUtils.isEmpty(stringRedisService.get(String.valueOf(sku.getSkuId())))){
                                    stockNumber=(int) stringRedisService.get(String.valueOf(sku.getSkuId()));
                                }else {
                                    stockNumber=map.get(sku.getSkuId()).getStockNumber();
                                }*/
                        stockNumber = map.get(sku.getSkuId()).getStockNumber();
                        if (sku.getNumber() > stockNumber) {
                            flag.set(true);
                        }
                    }
                });
            }
        }
        return map;
    }

    @Override
    protected void createShopOrders(Long parentId, OrderParam param, CereBuyerUser user, String time, Map<Long, CartSku> map,
                                    Map<Long, BigDecimal> shopPriceMap, Map<Long, CereBuyerShopCoupon> discountMap,
                                    Map<Long, BigDecimal> shopDeductCreditAmountMap, Map<Long, BigDecimal> platformCouponDiscountMap,
                                    Map<Long, BigDecimal> pricingDiscountPriceMap, PayUrl payUrl) throws Exception {
        //查询拼团活动数据
        CereShopGroupWork cereShopGroupWork = cereShopGroupWorkService.findById(param.getShopGroupWorkId());
        List<CartSku> skus = null;
        if (!EmptyUtils.isEmpty(map)) {
            //拿到所有商品数据
            skus = map.keySet().stream().map(map::get).collect(Collectors.toList());
        }
        //查询所有规格属性数据
        List<OrderProductAttribute> attributes = cereOrderProductAttributeService.findBySkus(skus);
        //查询收货地址数据
        CereBuyerReceive receive = cereBuyerReceiveService.findById(param.getReceiveId());
        if (receive != null) {
            for (OrderProductParam shop : param.getShops()) {
                //封装子订单数据
                CereShopOrder order = new CereShopOrder();
                order.setShopId(shop.getShopId());
                order.setParentId(parentId);
                order.setBuyerUserId(user.getBuyerUserId());
                order.setCreateTime(time);
                order.setUpdateTime(time);
                order.setCouponId(param.getCouponId());
                order.setReceiveName(receive.getReceiveName());
                order.setReceivePhone(receive.getReceivePhone());
                order.setReceiveAdress(receive.getReceiveAdress());
                order.setAddress(receive.getAddress());
                order.setCustomerName(user.getWechatName());
                order.setCustomerPhone(user.getPhone());
                order.setLogisticsPrice(shop.getDistribution().getDistributionPrice());
                order.setRemark(shop.getRemark());
                order.setLogisticsId(shop.getDistribution().getLogisticsId());
                order.setState(IntegerEnum.ORDER_STAY_PAY.getCode());
                order.setOrderFormid(RandomStringUtil.getRandomCode(15, 0));
                //定义订单优惠券金额（平台优惠金额/店铺总数+店铺优惠金额）
                BigDecimal discountPrice = BigDecimal.ZERO;
                //计算订单优惠券优惠金额
                if (!EmptyUtils.isEmpty(shop.getId()) && !EmptyUtils.isEmpty(discountMap)) {
                    order.setId(shop.getId());
                    if (!EmptyUtils.isEmpty(discountMap.get(shop.getId()))) {
                        CereBuyerShopCoupon cereBuyerShopCoupon = discountMap.get(shop.getId());
                        //更新店铺优惠券状态为已使用
                        cereBuyerShopCoupon.setUpdateTime(time);
                        cereBuyerShopCoupon.setState(IntegerEnum.COUPON_USE.getCode());
                        cereBuyerShopCouponService.updateState(cereBuyerShopCoupon);
                        //删除redis延时任务
                        stringRedisService.delete(StringEnum.SHOP_COUPON_OVERDUE.getCode() + "-" + cereBuyerShopCoupon.getId());
                        cereRedisKeyServcice.deleteByKey(StringEnum.SHOP_COUPON_OVERDUE.getCode() + "-" + cereBuyerShopCoupon.getId());
                        order.setShopOperateId(cereBuyerShopCoupon.getShopOperateId());
                        if (!EmptyUtils.isEmptyBigdecimal(discountPrice)) {
                            discountPrice = discountPrice.add(cereBuyerShopCoupon.getReduceMoney());
                        } else {
                            discountPrice = cereBuyerShopCoupon.getReduceMoney();
                        }
                    }
                }
                //加上平台优惠券优惠的部分
                discountPrice = discountPrice.add(platformCouponDiscountMap.getOrDefault(shop.getShopId(), BigDecimal.ZERO))
                        .add(shopDeductCreditAmountMap.getOrDefault(shop.getShopId(), BigDecimal.ZERO));
                order.setDiscountPrice(discountPrice);
                if (!EmptyUtils.isEmpty(skus)) {
                    order.setOrderPrice(shopPriceMap.get(shop.getShopId()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //订单支付金额=订单总金额+运费-计算出来的优惠金额
                    order.setPrice(order.getOrderPrice().add(order.getLogisticsPrice()).subtract(order.getDiscountPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    order.setPaymentState(IntegerEnum.ORDER_PAY_STAY.getCode());
                    order.setPaymentMode(param.getPaymentMode());

                    order.setBuyerUserId(user.getBuyerUserId());
                    order.setShopGroupWorkId(param.getShopGroupWorkId());
                    //插入订单数据
                    cereShopOrderDAO.insert(order);
                    //新增订单商品数据
                    addOrderProduct(skus, order.getOrderId(), shop.getShopId(), map, attributes);
                    Long collageId = param.getCollageId();
                    if (EmptyUtils.isEmpty(param.getCollageId())) {
                        //生成拼单数据
                        CereCollageOrder cereCollageOrder = new CereCollageOrder();
                        cereCollageOrder.setBuyerUserId(user.getBuyerUserId());
                        cereCollageOrder.setCreateTime(time);
                        cereCollageOrder.setShopGroupWorkId(param.getShopGroupWorkId());
                        cereCollageOrder.setSurplusNumber(cereShopGroupWork.getPerson());
                        cereCollageOrder.setState(IntegerEnum.COLLAGE_STATE_STAY.getCode());
                        cereCollageOrderService.insert(cereCollageOrder);
                        collageId = cereCollageOrder.getCollageId();
                        param.setCollageId(cereCollageOrder.getCollageId());
                        //设置该拼单截止时间
                        String end = TimeUtils.getMoreHourAfter(time, cereShopGroupWork.getEffectiveTime());

                        //设置30分钟延时自动取消订单,并且释放库存 拼单截止时间，至少一个小时，所以如果没支付，会先走取消的逻辑
                        stringRedisService.set(StringEnum.COLLAGE_UNPAY_CANCEL.getCode() + "-" + cereCollageOrder.getCollageId()
                                + "-" + order.getOrderId(), 1, 30 * 60 * 1000);
                        //新增延时任务记录
                        cereRedisKeyServcice.add(StringEnum.COLLAGE_UNPAY_CANCEL.getCode() + "-" + cereCollageOrder.getCollageId()
                                + "-" + order.getOrderId(), TimeUtils.getMinuteAfter(30));

                        //新增redis延时任务处理拼团失败
                        stringRedisService.set(StringEnum.COLLAGE_ERROR.getCode() + "-" + cereCollageOrder.getCollageId(), 1,
                                TimeUtils.getCountDownByTime(time, end));
                        //新增延时记录
                        cereRedisKeyServcice.add(StringEnum.COLLAGE_ERROR.getCode() + "-" + cereCollageOrder.getCollageId(), end);
                    }
                    //生成拼单明细数据
                    CereCollageOrderDetail detail = new CereCollageOrderDetail();
                    detail.setCollageId(collageId);
                    detail.setOrderId(order.getOrderId());
                    detail.setState(IntegerEnum.YES.getCode());
                    cereCollageOrderDetailService.insert(detail);
                    payUrl.setCollageId(collageId);
                }
            }
            //批量更新库存数量
            //cereProductSkuService.updateBatch(skus);
            /*for (CartSku sku:skus) {
                cereStockService.reduceStock(sku.getActivityType(), sku.getProductId(), sku.getSkuId(), sku.getNumber());
            }*/
            if (!EmptyUtils.isEmpty(skus) && ParamEnum.CART_SETTLEMENT.getCode().equals(param.getType())) {
                //删除购物车商品
                cereShopCartService.deleteSkus(skus, user.getBuyerUserId());
            }
            //插入规格属性数据
            if (!EmptyUtils.isEmpty(attributes)) {
                cereOrderProductAttributeService.insertBatch(attributes);
            }
        }
    }
}
