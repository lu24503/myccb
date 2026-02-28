package com.shop.cereshop.app.service.order.placeOrderTemplate;

import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.page.cart.CartSku;
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
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWork;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author pepis
 * @date 2021/12/9 18:42
 * @apiNote 拼团下单
 */
@Component
public class JoinPlaceOrder extends PlaceOrderTemplate {

    @Autowired
    private LaunchPlaceOrder launchPlaceOrder;


    public JoinPlaceOrder(CereBuyerShopCouponService cereBuyerShopCouponService, CerePlatformSeckillService cerePlatformSeckillService,
                          CerePlatformDiscountService cerePlatformDiscountService, CereShopSeckillService cereShopSeckillService,
                          CereShopDiscountService cereShopDiscountService, CereShopPriceService cereShopPriceService,
                          CereShopComposeService cereShopComposeService, CereStockService cereStockService, CereProductSkuService cereProductSkuService,
                          CereShopSceneMemberService cereShopSceneMemberService, CereProductMemberService cereProductMemberService,
                          CereBuyerCouponService cereBuyerCouponService, WxPayService wxPayService, UploadService uploadService,
                          CereOrderParentService cereOrderParentService, CereOrderProductService cereOrderProductService,
                          CereShopOrderDAO cereShopOrderDAO, StringRedisService stringRedisService, CereRedisKeyServcice cereRedisKeyServcice,
                          CereOrderProductAttributeService cereOrderProductAttributeService, CereBuyerReceiveService cereBuyerReceiveService,
                          CereShopCartService cereShopCartService, CereShopGroupWorkService cereShopGroupWorkService,
                          CereBuyerUserService cereBuyerUserService, CerePlatformActivityService cerePlatformActivityService, WxCardUtil wxCardUtil,
                          CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO, CereOrderLogisticsService cereOrderLogisticsService,
                          CereShopOrderService cereShopOrderService) {
        super(cereBuyerShopCouponService, cerePlatformSeckillService, cerePlatformDiscountService, cereShopSeckillService, cereShopDiscountService,
                cereShopPriceService, cereShopComposeService, cereStockService, cereProductSkuService, cereShopSceneMemberService,
                cereProductMemberService, cereBuyerCouponService, wxPayService, uploadService, cereOrderParentService, cereOrderProductService,
                cereShopOrderDAO, stringRedisService, cereRedisKeyServcice, cereOrderProductAttributeService, cereBuyerReceiveService,
                cereShopCartService, cereShopGroupWorkService, cereBuyerUserService, cerePlatformActivityService, wxCardUtil,
                cereSkuMemberRealInfoDAO, cereOrderLogisticsService, cereShopOrderService);
    }

    @Override
    protected void check(OrderParam param) throws CoBusinessException {
        //查询拼团活动数据
        CereShopGroupWork cereShopGroupWork = cereShopGroupWorkService.findById(param.getShopGroupWorkId());
        if (null == cereShopGroupWork || EmptyUtils.isEmpty(param.getShops())) {
            throw new CoBusinessException(CoReturnFormat.PARAM_INVALID);
        }
    }

//    public void placeOrder(OrderParam param, CereBuyerUser user, String ip, PayUrl payUrl, String time) throws Exception {
//        //发起拼团提交订单业务处理
//        //查询拼团活动数据
//        CereShopGroupWork cereShopGroupWork = cereShopGroupWorkService.findById(param.getShopGroupWorkId());
//        if (null == cereShopGroupWork || EmptyUtils.isEmpty(param.getShops())) {
//            return;
//        }
//        //定义map封装商品购买数量
//        Map<Long, ProductSku> numberMap = getBuySkuMap(param);
//
//        //校验库存 本次下单中的商品是否有库存不足
//        AtomicBoolean flag = new AtomicBoolean(false);
//        //计算运费
//        BigDecimal logisticPrice = calLogisticPrice(param);
////促销活动优惠金额
//        AtomicReference<BigDecimal> skuDiscountPrice = new AtomicReference<>(BigDecimal.ZERO);
//        //获取sku信息封装成 map
//        Map<Long, CartSku> map = getLongCartSkuMap(param, user,numberMap, flag,skuDiscountPrice);
//        //库存不足 请求失败
//        if (flag.get()) {
//            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
//        }
//        //计算订单总金额
//        BigDecimal orderPrice = calOrderPrice(map, numberMap);
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
//        //设置支付信息
//        payUrl.setMoney(parent.getPrice());
//        payUrl.setOrderId(parent.getParentId());
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
        launchPlaceOrder.createShopOrders(parentId, param, user, time, map, shopPriceMap, discountMap,
                shopDeductCreditAmountMap, platformCouponDiscountMap, pricingDiscountPriceMap, payUrl);
    }
}
