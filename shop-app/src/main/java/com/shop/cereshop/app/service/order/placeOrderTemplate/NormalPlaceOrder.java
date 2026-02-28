package com.shop.cereshop.app.service.order.placeOrderTemplate;

import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.app.page.order.PayUrl;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.param.compose.CereShopComposeDTO;
import com.shop.cereshop.app.param.discount.ShopPlatformDiscount;
import com.shop.cereshop.app.param.order.OrderParam;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessDiscount;
import com.shop.cereshop.app.param.seckill.ShopBusinessSeckill;
import com.shop.cereshop.app.param.seckill.ShopPlatformSeckill;
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
import com.shop.cereshop.commons.constant.ParamEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.domain.product.CereProductMember;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author pepis
 * @date 2021/12/9 17:33
 * @apiNote 普通下单
 */
@Component
@Slf4j
public class NormalPlaceOrder extends PlaceOrderTemplate {

    public NormalPlaceOrder(CereBuyerShopCouponService cereBuyerShopCouponService, CerePlatformSeckillService cerePlatformSeckillService,
                            CerePlatformDiscountService cerePlatformDiscountService, CereShopSeckillService cereShopSeckillService,
                            CereShopDiscountService cereShopDiscountService, CereShopPriceService cereShopPriceService,
                            CereShopComposeService cereShopComposeService, CereStockService cereStockService,
                            CereProductSkuService cereProductSkuService, CereShopSceneMemberService cereShopSceneMemberService,
                            CereProductMemberService cereProductMemberService, CereBuyerCouponService cereBuyerCouponService,
                            WxPayService wxPayService, UploadService uploadService, CereOrderParentService cereOrderParentService,
                            CereOrderProductService cereOrderProductService, CereShopOrderDAO cereShopOrderDAO,
                            StringRedisService stringRedisService, CereRedisKeyServcice cereRedisKeyServcice,
                            CereOrderProductAttributeService cereOrderProductAttributeService, CereBuyerReceiveService cereBuyerReceiveService,
                            CereShopCartService cereShopCartService, CereShopGroupWorkService cereShopGroupWorkService,
                            CereBuyerUserService cereBuyerUserService, CerePlatformActivityService cerePlatformActivityService,
                            WxCardUtil wxCardUtil, CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO,
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
        if (EmptyUtils.isEmpty(param.getShops())) {
            throw new CoBusinessException(CoReturnFormat.PARAM_INVALID);
        }
    }
//    @Override
//    public void placeOrder(OrderParam param, CereBuyerUser user, String ip, PayUrl payUrl, String time) throws Exception {
//
//        if (EmptyUtils.isEmpty(param.getShops())) {
//            return;
//        }
//        //定义map封装商品购买数量
//        Map<Long, ProductSku> numberMap = getBuySkuMap(param);
//        //校验库存 本次下单中的商品是否有库存不足
//        AtomicBoolean flag = new AtomicBoolean(false);
//        //计算运费
//        BigDecimal logisticPrice = calLogisticPrice(param);
//        //促销活动优惠金额
//        AtomicReference<BigDecimal> skuDiscountPrice = new AtomicReference<>(BigDecimal.ZERO);
//        Map<Long, CartSku> map = getLongCartSkuMap(param, user, numberMap, flag, skuDiscountPrice);
//
//        if (flag.get()) {
//            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
//        }
//
//        BigDecimal orderPrice = calOrderPrice(map, numberMap);
//        //减去促销活动优惠的金额
//        orderPrice = orderPrice.subtract(skuDiscountPrice.get());
//
//        //查询平台优惠券数据
//        CereBuyerCoupon buyerCoupon = getBuyerCoupon(param.getCouponId(), user.getBuyerUserId());
//        //定义店铺优惠金额map
//        Map<Long, CereBuyerShopCoupon> discountMap = new HashMap<>();
//        //计算优惠金额
//        BigDecimal discountPrice = setDiscountPrice(buyerCoupon, param, map, numberMap, discountMap, orderPrice);
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
//        //新增父订单数据
//        CereOrderParent parent = createParentOrder(payPrice, time, logisticPrice, orderPrice);
//
//        payUrl.setMoney(param.getPrice());
//        payUrl.setOrderId(parent.getParentId());
//
//        //生成支付二维码
//        generatePayUrl(param, user.getWechatOpenId(), parent.getParentFormid(), ip, payUrl);
//
//        //遍历店铺数据,新增订单
//        createShopOrders(parent.getParentId(), param, user, time, map, buyerCoupon, null, null, null);
//    }

    @Override
    public Map<Long, CartSku> getLongCartSkuMap(OrderParam param, CereBuyerUser user, Map<Long, ProductSku> numberMap, AtomicBoolean flag,
                                                AtomicReference<BigDecimal> skuDiscountPrice, Map<Long, BigDecimal> shopDiscountPriceMap) {

        Map<Long, CartSku> map = new HashMap<>();

        List<Long> shopIdList = param.getShops().stream().map(OrderProductParam::getShopId).collect(Collectors.toList());

        // 平台秒杀
        List<ShopPlatformSeckill> platformSeckillList = cerePlatformSeckillService.selectPlatformSeckillsByShopIdList(shopIdList);
        Map<Long, List<ShopPlatformSeckill>> platformSeckillMap = platformSeckillList.stream().collect(Collectors.groupingBy(ShopPlatformSeckill::getSeckillId));

        // 平台折扣
        List<ShopPlatformDiscount> platformDiscountList = cerePlatformDiscountService.selectPlatformDiscountByShopIdList(shopIdList);
        Map<Long, List<ShopPlatformDiscount>> platformDiscountMap = platformDiscountList.stream().collect(Collectors.groupingBy(ShopPlatformDiscount::getDiscountId));

        // 商家秒杀
        List<ShopBusinessSeckill> seckillList = cereShopSeckillService.selectByShopIdList(shopIdList);
        Map<Long, List<ShopBusinessSeckill>> skuIdSeckillMap = seckillList.stream().collect(Collectors.groupingBy(ShopBusinessSeckill::getSkuId));

        // 商家折扣
        List<ShopBusinessDiscount> discountList = cereShopDiscountService.selectByShopIdList(shopIdList);
        Map<Long, List<ShopBusinessDiscount>> skuIdDiscountMap = discountList.stream().collect(Collectors.groupingBy(ShopBusinessDiscount::getSkuId));

        // 定价捆绑
        Map<Long, Map<Long, List<CerePriceRule>>> priceMap = cereShopPriceService.selectPriceMap(shopIdList);

        // 组合捆绑
        List<CereShopComposeDTO> composeDTOList = cereShopComposeService.selectByShopIdList(shopIdList);
        Map<Long, List<CereShopComposeDTO>> composeMap = composeDTOList.stream().collect(Collectors.groupingBy(CereShopComposeDTO::getComposeId));

        for (OrderProductParam shop : param.getShops()) {
            shopDiscountPriceMap.put(shop.getShopId(), BigDecimal.ZERO);
            shopIdList.add(shop.getShopId());

            if (!EmptyUtils.isEmpty(shop.getSkus())) {
                //查询当前店铺所有购买商品的库存数据
                List<CartSku> productSkus = cereProductSkuService.findStockNumberBySkus(shop.getSkus());
                if (!EmptyUtils.isEmpty(productSkus)) {
                    productSkus.forEach(sku -> {
                        ProductSku paramSku = numberMap.get(sku.getSkuId());
                        //设置购买数量
                        sku.setNumber(paramSku.getNumber());
                        //设置选中状态
                        //sku.setSelected(paramSku.getSelected());
                        //设置店铺id
                        sku.setShopId(shop.getShopId());
                        //设置相关活动id
                        sku.setPlatformSeckillId(paramSku.getPlatformSeckillId());
                        sku.setPlatformDiscountId(paramSku.getPlatformDiscountId());
                        sku.setShopSeckillId(paramSku.getShopSeckillId());
                        sku.setShopDiscountId(paramSku.getShopDiscountId());
                        sku.setPriceId(paramSku.getPriceId());
                        sku.setComposeId(paramSku.getComposeId());
                        sku.setSceneId(paramSku.getSceneId());
                        sku.setUseMember(paramSku.isUseMember());
                        sku.setBuyerCouponId(paramSku.getBuyerCouponId());
                        sku.setBuyerShopCouponId(paramSku.getBuyerShopCouponId());
                        map.put(sku.getSkuId(), sku);
                    });
                }
                //修正库存
                for (ProductSku sku : shop.getSkus()) {
                    CartSku cartSku = map.get(sku.getSkuId());
                    sku.setWeight(cartSku.getWeight());
                    Long productId = cartSku.getProductId();
                    if (sku.getPlatformSeckillId() != null && sku.getPlatformSeckillId() > 0) {
                        ProductStockInfo stockInfo = cereStockService.getActivityProductStock(IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode(), productId);
                        if (stockInfo != null) {
                            cartSku.setStockNumber(stockInfo.getStockNumber());
                            cartSku.setActivityType(IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode());
                            cartSku.setActivityId(sku.getPlatformSeckillId());
                        }
                    } else if (sku.getPlatformDiscountId() != null && sku.getPlatformDiscountId() > 0) {
                        ProductStockInfo stockInfo = cereStockService.getActivityProductStock(IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode(), productId);
                        if (stockInfo != null) {
                            cartSku.setStockNumber(stockInfo.getStockNumber());
                            cartSku.setActivityType(IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode());
                            cartSku.setActivityId(sku.getPlatformDiscountId());
                        }
                    } else if (sku.getShopSeckillId() != null && sku.getShopSeckillId() > 0) {
                        ProductStockInfo stockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode(), sku.getSkuId());
                        if (stockInfo != null) {
                            cartSku.setStockNumber(stockInfo.getStockNumber());
                            cartSku.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode());
                            cartSku.setActivityId(sku.getShopSeckillId());
                        }
                    } else if (sku.getShopDiscountId() != null && sku.getShopDiscountId() > 0) {
                        ProductStockInfo stockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode(), sku.getSkuId());
                        if (stockInfo != null) {
                            cartSku.setStockNumber(stockInfo.getStockNumber());
                            cartSku.setActivityType(IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode());
                            cartSku.setActivityId(sku.getShopDiscountId());
                        }
                    }
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

                //应用平台秒杀id
                //applyPlatformSeckill();
                List<ProductSku> skuList = shop.getSkus().stream().filter(sku -> sku.getPlatformSeckillId() != null && sku.getPlatformSeckillId() > 0).collect(Collectors.toList());
                Map<Long, List<ProductSku>> skuMap = skuList.stream().collect(Collectors.groupingBy(ProductSku::getPlatformSeckillId));
                BigDecimal tmpDiscountPrice = BigDecimal.ZERO;
                for (Long id : skuMap.keySet()) {
                    List<ShopPlatformSeckill> secList = platformSeckillMap.get(id);
                    if (CollectionUtils.isNotEmpty(secList)) {
                        ShopPlatformSeckill sec = secList.get(0);
                        //long num = skuMap.get(id).stream().mapToLong(ProductSku::getNumber).sum();
                        for (ProductSku sku : skuMap.get(id)) {
                            CartSku cartSku = map.get(sku.getSkuId());
                            cartSku.setPrice(cartSku.getOriginalPrice().subtract(sec.getSeckillMoney()));
                            if (cartSku.getOriginalPrice().compareTo(BigDecimal.ZERO) < 0) {
                                cartSku.setPrice(BigDecimal.ZERO);
                            }
                            cartSku.setActualPrice(cartSku.getPrice().multiply(new BigDecimal(cartSku.getNumber())));
                        }
                        //tmpDiscountPrice = sec.getSeckillMoney().multiply(new BigDecimal(num));
                        //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                    }
                }

                //应用平台折扣id
                //applyPlatformDiscount();
                skuList = shop.getSkus().stream().filter(sku -> sku.getPlatformDiscountId() != null && sku.getPlatformDiscountId() > 0).collect(Collectors.toList());
                skuMap = skuList.stream().collect(Collectors.groupingBy(ProductSku::getPlatformDiscountId));
                tmpDiscountPrice = BigDecimal.ZERO;
                for (Long id : skuMap.keySet()) {
                    List<ShopPlatformDiscount> disList = platformDiscountMap.get(id);
                    if (CollectionUtils.isNotEmpty(disList)) {
                        ShopPlatformDiscount discount = disList.get(0);

                        List<ProductSku> disSkuList = skuMap.get(id);
                        tmpDiscountPrice = BigDecimal.ZERO;
                        for (ProductSku disSku : disSkuList) {
                            CartSku cartSku = map.get(disSku.getSkuId());
                            cartSku.setPrice(cartSku.getOriginalPrice().multiply(discount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                                /*tmpDiscountPrice = tmpDiscountPrice.add(map.get(disSku.getSkuId()).getPrice().multiply(new BigDecimal(disSku.getNumber()))
                                        .multiply(BigDecimal.TEN.subtract(discount.getDiscount())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));*/
                        }
                        //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                    }
                }

                //应用商家秒杀id
                //applyShopSeckill();
                skuList = shop.getSkus().stream().filter(sku -> sku.getShopSeckillId() != null && sku.getShopSeckillId() > 0).collect(Collectors.toList());
                tmpDiscountPrice = BigDecimal.ZERO;
                for (ProductSku tmpSku : skuList) {
                    List<ShopBusinessSeckill> list = skuIdSeckillMap.get(tmpSku.getSkuId());
                    if (CollectionUtils.isNotEmpty(list)) {
                        ShopBusinessSeckill seckill = list.get(0);
                        CartSku cartSku = map.get(tmpSku.getSkuId());
                        cartSku.setPrice(seckill.getSeckillPrice());
                        //tmpDiscountPrice = map.get(tmpSku.getSkuId()).getPrice().subtract(seckill.getSeckillPrice()).multiply(new BigDecimal(tmpSku.getNumber()));
                        //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                    }
                }

                //应用商家折扣id
                //applyShopDiscount();
                skuList = shop.getSkus().stream().filter(sku -> sku.getShopDiscountId() != null && sku.getShopDiscountId() > 0).collect(Collectors.toList());
                tmpDiscountPrice = BigDecimal.ZERO;
                for (ProductSku tmpSku : skuList) {
                    List<ShopBusinessDiscount> list = skuIdDiscountMap.get(tmpSku.getSkuId());
                    if (CollectionUtils.isNotEmpty(list)) {
                        ShopBusinessDiscount dis = list.get(0);
                        CartSku cartSku = map.get(tmpSku.getSkuId());
                        cartSku.setPrice(cartSku.getOriginalPrice().multiply(dis.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                        //tmpDiscountPrice = map.get(tmpSku.getSkuId()).getPrice().subtract(dis.getPrice()).multiply(new BigDecimal(tmpSku.getNumber()));
                        //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                    }
                }

                //应用定价捆绑
                //applyPriceCombine();
                skuList = shop.getSkus().stream().filter(sku -> sku.getPriceId() != null && sku.getPriceId() > 0).collect(Collectors.toList());
                skuMap = skuList.stream().collect(Collectors.groupingBy(ProductSku::getPriceId));
                tmpDiscountPrice = BigDecimal.ZERO;
                Map<Long, List<CerePriceRule>> shopPriceMap = priceMap.get(shop.getShopId());
                for (Long id : skuMap.keySet()) {
                    List<CerePriceRule> disList = shopPriceMap.get(id);
                    if (CollectionUtils.isNotEmpty(disList)) {
                        List<ProductSku> tmpSkuList = skuMap.get(id);
                        BigDecimal originalPrice = BigDecimal.ZERO;
                        for (ProductSku tmpSku : tmpSkuList) {
                            originalPrice = originalPrice.add(new BigDecimal(tmpSku.getNumber()).multiply(map.get(tmpSku.getSkuId()).getPrice()));
                        }
                        int num = tmpSkuList.stream().mapToInt(ProductSku::getNumber).sum();
                        tmpSkuList.sort((o1, o2) -> {
                            CartSku sku1 = map.get(o1.getSkuId());
                            CartSku sku2 = map.get(o2.getSkuId());
                            BigDecimal price1 = BigDecimal.ZERO;
                            BigDecimal price2 = BigDecimal.ZERO;
                            if (sku1 != null) {
                                price1 = sku1.getPrice();
                            }
                            if (sku2 != null) {
                                price2 = sku2.getPrice();
                            }
                            return price1.compareTo(price2);
                        });

                        for (CerePriceRule rule : disList) {
                            if (num >= rule.getNumber()) {
                                int multiple = num / rule.getNumber();
                                //按照规则优惠后，可能存在部分数量的sku未享受到优惠
                                int unDiscountNum = num - multiple * rule.getNumber();
                                // 没有享受到优惠的部分sku的价格
                                BigDecimal unDiscountPrice = BigDecimal.ZERO;
                                for (ProductSku tmpSku : tmpSkuList) {
                                    if (unDiscountNum >= tmpSku.getNumber()) {
                                        unDiscountPrice = unDiscountPrice.add(map.get(tmpSku.getSkuId()).getPrice().multiply(new BigDecimal(tmpSku.getNumber())));
                                        unDiscountNum -= tmpSku.getNumber();
                                    } else {
                                        unDiscountPrice = unDiscountPrice.add(map.get(tmpSku.getSkuId()).getPrice().multiply(new BigDecimal(unDiscountNum)));
                                        unDiscountNum = 0;
                                    }
                                    if (unDiscountNum <= 0) {
                                        break;
                                    }
                                }
                                // multiple次 定价的价格
                                BigDecimal ruleDiscountPrice = rule.getPrice().multiply(new BigDecimal(multiple));
                                BigDecimal payPrice = ruleDiscountPrice.add(unDiscountPrice).setScale(BigDecimal.ROUND_HALF_UP, 2);
                                tmpDiscountPrice = originalPrice.subtract(payPrice);
                                //对于tmpSkuList里面的sku来说，优惠了 tmpDiscountPrice，所以实际支付是 ruleDiscountPrice + unDiscountPrice，退款时定价捆绑是一起退的，所以直接放在第一个即可
                                map.get(tmpSkuList.get(0).getSkuId()).setActualPrice(payPrice);
                                //其它sku设置actualPrice = 0
                                for (int i = 1; i < tmpSkuList.size(); i++) {
                                    map.get(tmpSkuList.get(i).getSkuId()).setActualPrice(BigDecimal.ZERO);
                                }

                                shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                                break;
                            }
                        }
                        //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                    }
                }

                //应用组合捆绑
                //applyComposeCombine();
                skuList = shop.getSkus().stream().filter(sku -> sku.getComposeId() != null && sku.getComposeId() > 0).collect(Collectors.toList());
                skuMap = skuList.stream().collect(Collectors.groupingBy(ProductSku::getComposeId));
                for (Long id : skuMap.keySet()) {
                    List<CereShopComposeDTO> list = composeMap.get(id);
                    List<ProductSku> tmpSkuList = skuMap.get(id);
                    BigDecimal originalPrice = BigDecimal.ZERO;
                    for (ProductSku tmpSku : tmpSkuList) {
                        originalPrice = originalPrice.add(new BigDecimal(tmpSku.getNumber()).multiply(map.get(tmpSku.getSkuId()).getPrice()));
                    }

                    int totalNum = tmpSkuList.stream().mapToInt(ProductSku::getNumber).sum();
                    int minNum = tmpSkuList.stream().mapToInt(ProductSku::getNumber).min().getAsInt();
                    CereShopComposeDTO composeDTO = list.get(0);
                    if (composeDTO.getComposeType() == 1) {
                        tmpDiscountPrice = originalPrice.subtract(composeDTO.getPromote().multiply(new BigDecimal(minNum)));
                    } else if (composeDTO.getComposeType() == 2) {
                        tmpDiscountPrice = composeDTO.getPromote().multiply(new BigDecimal(minNum));
                    } else {
                        BigDecimal discountPrice = BigDecimal.ZERO;
                        for (ProductSku sku : tmpSkuList) {
                            discountPrice = discountPrice.add(map.get(sku.getSkuId()).getPrice().multiply(new BigDecimal(minNum)).multiply(BigDecimal.TEN.subtract(composeDTO.getPromote())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                        }
                        tmpDiscountPrice = discountPrice;
                    }
                    BigDecimal eachPrice = originalPrice.subtract(tmpDiscountPrice).divide(new BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP);
                    for (ProductSku tmpSku : tmpSkuList) {
                        map.get(tmpSku.getSkuId()).setPrice(eachPrice);
                    }
                    //tmpDiscountPrice = originalPrice.subtract(eachPrice.multiply(new BigDecimal(totalNum)));
                    //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));
                }

                //应用场景营销
                //applySceneMarket();
                skuList = shop.getSkus().stream().filter(sku -> sku.getSceneId() != null && sku.getSceneId() > 0).collect(Collectors.toList());
                //tmpDiscountPrice = BigDecimal.ZERO;
                CereShopSceneMember sceneMember = null;
                for (ProductSku sku : skuList) {
                    if (sceneMember == null) {
                        sceneMember = cereShopSceneMemberService.selectSceneMemberList(sku.getSceneId(), user.getMemberLevelId());
                    }
                    if (sceneMember != null) {
                        BigDecimal skuPrice = map.get(sku.getSkuId()).getPrice();
                        skuPrice = skuPrice.multiply(sceneMember.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                        map.get(sku.getSkuId()).setPrice(skuPrice);
                        //tmpDiscountPrice = tmpDiscountPrice.add(map.get(sku.getSkuId()).getPrice().multiply(new BigDecimal(sku.getNumber())).multiply(BigDecimal.TEN.subtract(sceneMember.getDiscount())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
                //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));

                //应用会员价
                //applyMember();
                skuList = shop.getSkus().stream().filter(ProductSku::isUseMember).collect(Collectors.toList());
                //tmpDiscountPrice = BigDecimal.ZERO;
                for (ProductSku sku : skuList) {
                    CereProductMember productMember = cereProductMemberService.selectProductMember(user.getMemberLevelId(), map.get(sku.getSkuId()).getProductId(), sku.getSkuId());
                    if (productMember != null) {
                        //BigDecimal num = new BigDecimal(sku.getNumber());
                        if (IntegerEnum.MEMBER_PRODUCT_MODE_PRICE.getCode().equals(productMember.getMode())) {
                            map.get(sku.getSkuId()).setPrice(productMember.getPrice());
                            //sku的价格必须 > 会员价
                                /*if (map.get(sku.getSkuId()).getPrice().compareTo(productMember.getPrice()) > 0) {
                                    tmpDiscountPrice = tmpDiscountPrice.add(map.get(sku.getSkuId()).getPrice().subtract(productMember.getPrice()).multiply(num));
                                }*/
                        } else {
                            BigDecimal skuPrice = map.get(sku.getSkuId()).getPrice();
                            skuPrice = skuPrice.multiply(productMember.getPrice()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                            map.get(sku.getSkuId()).setPrice(skuPrice);
                            // 打折默认折扣都是正常的 价格 x 数量 x (10-折扣) / 10 就是优惠价
                            //tmpDiscountPrice = tmpDiscountPrice.add(map.get(sku.getSkuId()).getPrice().multiply(num).multiply(BigDecimal.TEN.subtract(productMember.getPrice())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                //shopDiscountPriceMap.put(shop.getShopId(), shopDiscountPriceMap.get(shop.getShopId()).add(tmpDiscountPrice));

                for (Long skuId : map.keySet()) {
                    Long priceId = map.get(skuId).getPriceId();
                    //设置非定价捆绑的实付价格
                    if (priceId == null || priceId == 0) {
                        map.get(skuId).setActualPrice(map.get(skuId).getPrice().multiply(new BigDecimal(map.get(skuId).getNumber())));
                    }
                }

                //应用普通价
                //applyNormal();
            }
        }
        skuDiscountPrice.set(shopDiscountPriceMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        return map;
    }


    @Override
    protected void createShopOrders(Long parentId, OrderParam param, CereBuyerUser user, String time, Map<Long, CartSku> map,
                                    Map<Long, BigDecimal> shopPriceMap, Map<Long, CereBuyerShopCoupon> discountMap,
                                    Map<Long, BigDecimal> shopDeductCreditAmountMap, Map<Long, BigDecimal> platformCouponDiscountMap,
                                    Map<Long, BigDecimal> pricingDiscountPriceMap, PayUrl payUrl) throws Exception {
        //遍历店铺
        List<CartSku> skus = null;
        if (!EmptyUtils.isEmpty(map)) {
            //拿到所有商品数据
            skus = map.keySet().stream().map(map::get).collect(Collectors.toList());
        }
        //查询所有规格属性数据
        List<OrderProductAttribute> attributes = cereOrderProductAttributeService.findBySkus(skus);
        //查询收货地址数据
        CereBuyerReceive receive = cereBuyerReceiveService.findById(param.getReceiveId());
        if (receive != null && skus != null) {
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
                Long seckillId = shop.getSkus().stream().filter(obj -> obj.getPlatformSeckillId() != null).findAny().map(ProductSku::getPlatformSeckillId).orElse(null);
                Long discountId = shop.getSkus().stream().filter(obj -> obj.getPlatformDiscountId() != null).findAny().map(ProductSku::getPlatformDiscountId).orElse(null);
                order.setSeckillId(seckillId);
                order.setDiscountId(discountId);
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
                //加上平台优惠券优惠的部分，加上积分优惠的部分
                discountPrice = discountPrice.add(platformCouponDiscountMap.getOrDefault(shop.getShopId(), BigDecimal.ZERO))
                        .add(shopDeductCreditAmountMap.getOrDefault(shop.getShopId(), BigDecimal.ZERO));
                order.setDiscountPrice(discountPrice);
                if (!EmptyUtils.isEmpty(skus)) {
                    order.setOrderPrice(shopPriceMap.get(shop.getShopId()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //订单支付金额=订单总金额+运费-计算出来的优惠金额
                    order.setPrice(order.getOrderPrice().add(order.getLogisticsPrice()).subtract(order.getDiscountPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //设置定价捆绑优惠金额
                    order.setPricingPrice(pricingDiscountPriceMap.getOrDefault(shop.getShopId(), BigDecimal.ZERO));
                    order.setPaymentState(IntegerEnum.ORDER_PAY_STAY.getCode());
                    order.setPaymentMode(param.getPaymentMode());
                    order.setBuyerUserId(user.getBuyerUserId());
                    //插入订单数据
                    cereShopOrderDAO.insert(order);
                    //新增订单商品数据
                    addOrderProduct(skus, order.getOrderId(), shop.getShopId(), map, attributes);
                    //设置30分钟延时自动取消订单,并且释放库存
                    stringRedisService.set(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId(), 1, 30 * 60 * 1000);
                    //新增延时任务记录
                    cereRedisKeyServcice.add(StringEnum.ORDER_AUTOMATIC_CANCEL.getCode() + "-" + order.getOrderId(), TimeUtils.getMinuteAfter(30));
                }
            }
            //批量更新库存数量
            //cereProductSkuService.updateBatch(skus);
            List<CartSku> dealedSkus = new ArrayList<>();
            if (!EmptyUtils.isEmpty(skus)) {
                for (CartSku sku : skus) {
                    try {
                        cereStockService.reduceStock(sku.getActivityType(), sku.getActivityId(), sku.getProductId(), sku.getSkuId(), sku.getNumber(), sku.getIfOversold());
                        dealedSkus.add(sku);
                    } catch (CoBusinessException cbe) {
                        log.error("reduceStock failed: activityType = {}, activityId = {}, productId = {}, skuId = {}, buyNumber = {}, ifOversold = {},",
                                sku.getActivityType(), sku.getActivityId(), sku.getProductId(), sku.getSkuId(), sku.getNumber(), sku.getIfOversold(), cbe);
                        for (CartSku dSku : dealedSkus) {
                            cereStockService.rollbackStock(dSku.getActivityType(), dSku.getActivityId(), dSku.getProductId(), dSku.getSkuId(), dSku.getNumber());
                        }
                    }
                }
            }
            //插入规格属性数据
            if (!EmptyUtils.isEmpty(attributes)) {
                cereOrderProductAttributeService.insertBatch(attributes);
            }
        }
        if (!EmptyUtils.isEmpty(skus) && ParamEnum.CART_SETTLEMENT.getCode().equals(param.getType())) {
            //删除购物车商品
            cereShopCartService.deleteSkus(skus, user.getBuyerUserId());
        }
    }

}
