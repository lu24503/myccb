package com.shop.cereshop.app.service.order.placeOrderTemplate;

import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.app.page.order.PayUrl;
import com.shop.cereshop.app.page.settlement.Distribution;
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
import com.shop.cereshop.app.utils.ImageUtil;
import com.shop.cereshop.commons.constant.*;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import com.shop.cereshop.commons.domain.order.CereOrderParent;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author pepis
 * @date 2021/12/9 17:13
 * @apiNote 下单模板抽象
 */
@Component
@RequiredArgsConstructor
public abstract class PlaceOrderTemplate {

    protected final CereBuyerShopCouponService cereBuyerShopCouponService;

    protected final CerePlatformSeckillService cerePlatformSeckillService;

    protected final CerePlatformDiscountService cerePlatformDiscountService;

    protected final CereShopSeckillService cereShopSeckillService;

    protected final CereShopDiscountService cereShopDiscountService;

    protected final CereShopPriceService cereShopPriceService;

    protected final CereShopComposeService cereShopComposeService;

    protected final CereStockService cereStockService;

    protected final CereProductSkuService cereProductSkuService;

    protected final CereShopSceneMemberService cereShopSceneMemberService;

    protected final CereProductMemberService cereProductMemberService;

    protected final CereBuyerCouponService cereBuyerCouponService;

    protected final WxPayService wxPayService;

    protected final UploadService uploadService;

    protected final CereOrderParentService cereOrderParentService;

    protected final CereOrderProductService cereOrderProductService;

    protected final CereShopOrderDAO cereShopOrderDAO;

    protected final StringRedisService stringRedisService;

    protected final CereRedisKeyServcice cereRedisKeyServcice;

    protected final CereOrderProductAttributeService cereOrderProductAttributeService;

    protected final CereBuyerReceiveService cereBuyerReceiveService;

    protected final CereShopCartService cereShopCartService;

    protected final CereShopGroupWorkService cereShopGroupWorkService;

    protected final CereBuyerUserService cereBuyerUserService;

    protected final CerePlatformActivityService cerePlatformActivityService;

    protected final WxCardUtil wxCardUtil;

    protected final CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO;

    protected final CereOrderLogisticsService cereOrderLogisticsService;

    protected final CereShopOrderService cereShopOrderService;

    /**
     * 定义下单模板方法 流程如下
     *
     * @param param
     * @param user
     * @param ip
     * @param time
     * @throws Exception
     */
    public final PayUrl placeOrder(OrderParam param, CereBuyerUser user, String ip, String time) throws Exception {

        //校验数据
        check(param);
        Long buyerUserId = user.getBuyerUserId();
        //定义map封装商品购买数量
        Map<Long, ProductSku> paramSkuMap = getBuySkuMap(param);

        //校验库存 本次下单中的商品是否有库存不足
        AtomicBoolean flag = new AtomicBoolean(false);

        //促销活动优惠金额
        AtomicReference<BigDecimal> skuDiscountPrice = new AtomicReference<>(BigDecimal.ZERO);
        //促销活动优惠，每个商家各优惠多少，当前只存储定价捆绑的优惠
        Map<Long, BigDecimal> pricingDiscountPriceMap = new HashMap<>();
        //获取sku信息封装成 map
        Map<Long, CartSku> map = getLongCartSkuMap(param, user, paramSkuMap, flag, skuDiscountPrice, pricingDiscountPriceMap);
        //库存不足 请求失败
        if (flag.get()) {
            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
        }
        //每个店铺的商品金额
        Map<Long, BigDecimal> shopPriceMap = new HashMap<>();
        //计算订单总金额
        BigDecimal orderPrice = calOrderPrice(map, paramSkuMap, shopPriceMap);
        //结合促销活动优惠金额计算，子订单的orderPrice
        for (Long shopId : pricingDiscountPriceMap.keySet()) {
            BigDecimal shopDiscountPrice = pricingDiscountPriceMap.get(shopId);
            if (shopDiscountPrice != null) {
                shopPriceMap.put(shopId, shopPriceMap.get(shopId).subtract(shopDiscountPrice));
            }
        }

        for (OrderProductParam shop:param.getShops()) {
            List<CereOrderLogistics> logistics = cereOrderLogisticsService.findLogistics(shop.getShopId());
            Long sceneId = shop.getSkus().stream().map(ProductSku::getSceneId).filter(Objects::nonNull).findFirst().orElse(0L);
            boolean needFilterScene = false;
            if (sceneId > 0) {
                CereShopSceneMember sceneMember = cereShopSceneMemberService.selectSceneMemberList(sceneId, user.getMemberLevelId());
                if (sceneMember != null) {
                    if (IntegerEnum.YES.getCode().equals(sceneMember.getIfFreeShipping())) {
                        needFilterScene = true;
                    }
                }
            }

            List<CartSku> skuList = shop.getSkus().stream().map(this::convertCartSku).collect(Collectors.toList());
            Map<Long, Integer> numberMap = shop.getSkus().stream().collect(Collectors.toMap(ProductSku::getSkuId, ProductSku::getNumber));
            if (needFilterScene) {
                skuList = shop.getSkus().stream().filter(obj -> obj.getSceneId() == null || obj.getSceneId() == 0L).map(this::convertCartSku).collect(Collectors.toList());
            }
            CereBuyerReceive receive = cereBuyerReceiveService.findById(param.getReceiveId());
            Distribution distribution = cereShopOrderService.setLogisticPrice(logistics, skuList, receive, numberMap);
            if (distribution == null) {
                distribution = new Distribution();
                distribution.setDistributionName("全国包邮");
                distribution.setDistributionPrice(BigDecimal.ZERO);
                distribution.setLogisticsId(-1L);
                shop.setDistribution(distribution);
            } else {
                shop.setDistribution(distribution);
            }
        }

        //计算运费
        BigDecimal logisticPrice = calLogisticPrice(param);
        //订单金额 - 营销活动优惠(例如：定价捆绑)
        orderPrice = orderPrice.subtract(skuDiscountPrice.get());

        //查询平台优惠券数据
        CereBuyerCoupon cereBuyerCoupon = getBuyerCoupon(param.getCouponId(), user.getBuyerUserId());

        //定义店铺优惠券map
        Map<Long, CereBuyerShopCoupon> discountMap = new HashMap<>();

        //定义店铺积分扣减map
        Map<Long, BigDecimal> shopDeductCreditAmountMap = new HashMap<>();

        //平台优惠券-各商家优惠占比
        Map<Long, BigDecimal> platformCouponDiscountMap = new HashMap<>();

        //计算优惠券优惠金额
        BigDecimal discountPrice = setDiscountPrice(cereBuyerCoupon, param, map, paramSkuMap, discountMap, orderPrice, shopPriceMap, platformCouponDiscountMap, pricingDiscountPriceMap);
        param.setDiscountPrice(discountPrice);

        //先计算平台优惠券优惠之后再加上运费
        orderPrice = orderPrice.add(logisticPrice);

        //减去积分抵扣金额
        BigDecimal deductCreditAmount = BigDecimal.ZERO;
        int deductCredit = 0;
        for (Long skuId : paramSkuMap.keySet()) {
            ProductSku sku = paramSkuMap.get(skuId);
            if (sku.getUseCreditAmount() != null && !sku.getUseCreditAmount().equals(BigDecimal.ZERO)) {
                deductCreditAmount = deductCreditAmount.add(sku.getUseCreditAmount());
                deductCredit += sku.getUseCredit();
                //将两个值设置到map中
                map.get(sku.getSkuId()).setUseCredit(sku.getUseCredit());
                map.get(sku.getSkuId()).setUseCreditAmount(sku.getUseCreditAmount());

                Long shopId = map.get(sku.getSkuId()).getShopId();
                shopDeductCreditAmountMap.putIfAbsent(shopId, BigDecimal.ZERO);
                shopDeductCreditAmountMap.put(shopId, shopDeductCreditAmountMap.get(shopId).add(deductCreditAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }

        //计算订单实付金额=订单总金额-优惠-积分抵扣
        BigDecimal payPrice = orderPrice.subtract(discountPrice).subtract(deductCreditAmount);
        param.setPrice(payPrice);
        //校验实付金额
        if (EmptyUtils.isEmptyBigdecimal(payPrice)) {
            //如果金额为0,提示
            throw new CoBusinessException(CoReturnFormat.PAY_MONEY_NOT_ZERO);
        }

        //分摊运费
        calcLogistics(param, map);

        //新增父订单数据
        CereOrderParent parent = createParentOrder(payPrice, time, logisticPrice, orderPrice);

        //设置支付信息
        PayUrl payUrl = new PayUrl();
        payUrl.setMoney(parent.getPrice());
        payUrl.setOrderId(parent.getParentId());
        //生成支付二维码
        generatePayUrl(param, user.getWechatOpenId(), parent.getParentFormid(), ip, payUrl);

        //扣减积分
        if (!deductCreditAmount.equals(BigDecimal.ZERO)) {
            cereBuyerUserService.decreaseCredit(user.getBuyerUserId(), deductCredit, CreditOptTypeEnum.ORDER_DEDUCT);
        }

        //遍历店铺数据,新增订单
        createShopOrders(parent.getParentId(), param, user, time, map, shopPriceMap,
                discountMap, shopDeductCreditAmountMap, platformCouponDiscountMap, pricingDiscountPriceMap, payUrl);

        //更新商品的下单人数字段
        List<Long> productIdList = map.values().stream().map(CartSku::getProductId).distinct().collect(Collectors.toList());
        List<Long> updateProductIdList = new ArrayList<>();
        for (Long productId : productIdList) {
            int updateCnt = cereShopOrderDAO.saveBuyerProductRecord(buyerUserId, productId);
            System.out.println("updateCnt " + updateCnt);
            if (updateCnt > 0) {
                System.out.println("updateCnt > 0");
                updateProductIdList.add(productId);
            }
        }

        if (updateProductIdList.size() > 0) {
            cereSkuMemberRealInfoDAO.increSalesUserCount(updateProductIdList);
        }

        //更新平台优惠券状态
        useBuyerCoupon(cereBuyerCoupon, time);

        return payUrl;
    }

    /**
     * 计算运费
     *
     * @param param
     * @param skuMap
     */
    private void calcLogistics(OrderParam param, Map<Long, CartSku> skuMap) {
        for (OrderProductParam shop : param.getShops()) {
            if (shop.getDistribution() != null
                    && shop.getDistribution().getDistributionPrice() != null
                    && shop.getDistribution().getDistributionPrice().compareTo(BigDecimal.ZERO) != 0) {
                Integer chargeType = ParamEnum.getByName(shop.getDistribution().getDistributionName());
                int totalNum = shop.getSkus().stream().mapToInt(ProductSku::getNumber).sum();
                BigDecimal totalWeight = shop.getSkus().stream().map(sku -> {
                    CartSku cartSku = skuMap.get(sku.getSkuId());
                    if (cartSku != null && cartSku.getWeight() != null) {
                        return cartSku.getWeight().multiply(new BigDecimal(cartSku.getNumber()));
                    }
                    return BigDecimal.ZERO;
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal remainLogisticsPrice = shop.getDistribution().getDistributionPrice();

                if (ParamEnum.CHARGE_TYPE_NUMBER.getCode().equals(chargeType)) {
                    for (int i = 0; i < shop.getSkus().size(); i++) {
                        ProductSku sku = shop.getSkus().get(i);
                        CartSku cartSku = skuMap.get(sku.getSkuId());
                        if (cartSku == null) {
                            continue;
                        }
                        if (sku.getNumber() == totalNum) {
                            cartSku.setLogisticsPrice(shop.getDistribution().getDistributionPrice());
                            break;
                        }
                        double percent = sku.getNumber() / 1.0 / totalNum;
                        BigDecimal curLogisticsPrice = shop.getDistribution().getDistributionPrice().multiply(new BigDecimal(percent))
                                .setScale(2, BigDecimal.ROUND_HALF_UP);

                        // 最后一件的时候，可能计算有偏差，直接将剩下的运费都给到最后一件
                        if (i == shop.getSkus().size() - 1) {
                            curLogisticsPrice = remainLogisticsPrice;
                        } else if (curLogisticsPrice.compareTo(remainLogisticsPrice) > 0) {
                            curLogisticsPrice = remainLogisticsPrice;
                        }
                        cartSku.setLogisticsPrice(curLogisticsPrice);
                        remainLogisticsPrice = remainLogisticsPrice.subtract(curLogisticsPrice);
                    }
                } else if (ParamEnum.CHARGE_TYPE_WEIGHT.getCode().equals(chargeType)) {
                    for (int i = 0; i < shop.getSkus().size(); i++) {
                        ProductSku sku = shop.getSkus().get(i);
                        CartSku cartSku = skuMap.get(sku.getSkuId());
                        if (cartSku == null) {
                            continue;
                        }
                        if (cartSku.getWeight() == null || cartSku.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            cartSku.setLogisticsPrice(BigDecimal.ZERO);
                            continue;
                        }
                        BigDecimal skuTotalWeight = cartSku.getWeight().multiply(new BigDecimal(cartSku.getNumber()));
                        if (skuTotalWeight.compareTo(totalWeight) == 0) {
                            cartSku.setLogisticsPrice(shop.getDistribution().getDistributionPrice());
                            break;
                        }
                        BigDecimal percent = skuTotalWeight.divide(totalWeight, 6, BigDecimal.ROUND_HALF_UP);
                        BigDecimal curLogisticsPrice = shop.getDistribution().getDistributionPrice().multiply(percent)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);

                        // 最后一件的时候，可能计算有偏差，直接将剩下的运费都给到最后一件
                        if (i == shop.getSkus().size() - 1) {
                            curLogisticsPrice = remainLogisticsPrice;
                        } else if (curLogisticsPrice.compareTo(remainLogisticsPrice) > 0) {
                            curLogisticsPrice = remainLogisticsPrice;
                        }
                        cartSku.setLogisticsPrice(curLogisticsPrice);
                        remainLogisticsPrice = remainLogisticsPrice.subtract(curLogisticsPrice);
                    }
                }
            } else {
                for (ProductSku sku : shop.getSkus()) {
                    Long skuId = sku.getSkuId();
                    if (skuMap.get(skuId) != null) {
                        skuMap.get(skuId).setLogisticsPrice(BigDecimal.ZERO);
                    }
                }
            }
        }
        // 防止有些数据没有设置
        for (CartSku sku : skuMap.values()) {
            if (sku.getLogisticsPrice() == null) {
                sku.setLogisticsPrice(BigDecimal.ZERO);
            }
        }
    }

    /**
     * 校验数据抽象方法 不同下单方式校验不同
     *
     * @param param /
     */
    protected abstract void check(OrderParam param) throws CoBusinessException;

    /**
     * 创建子订单
     *
     * @param parentId    父订单id
     * @param param       结算请求实体
     * @param user        客户
     * @param time        时间
     * @param map         skuId与sku实体组成map
     * @param discountMap 店铺优惠券数据
     * @param payUrl      支付信息
     * @throws Exception /
     */
    protected abstract void createShopOrders(Long parentId, OrderParam param, CereBuyerUser user, String time, Map<Long, CartSku> map,
                                             Map<Long, BigDecimal> shopPriceMap, Map<Long, CereBuyerShopCoupon> discountMap,
                                             Map<Long, BigDecimal> shopDeductCreditAmountMap, Map<Long, BigDecimal> platformCouponDiscountMap,
                                             Map<Long, BigDecimal> pricingDiscountPriceMap, PayUrl payUrl) throws Exception;


    /**
     * 组装 sku信息map
     *
     * @param param
     * @param user
     * @param numberMap
     * @param flag
     * @param skuDiscountPrice
     * @return
     */
    protected abstract Map<Long, CartSku> getLongCartSkuMap(OrderParam param, CereBuyerUser user, Map<Long, ProductSku> numberMap,
                                                            AtomicBoolean flag, AtomicReference<BigDecimal> skuDiscountPrice,
                                                            Map<Long, BigDecimal> shopDiscountPriceMap);

    /**
     * 计算运费
     *
     * @param param
     * @return
     */
    private BigDecimal calLogisticPrice(OrderParam param) {
        BigDecimal totalLogisticsPrice = BigDecimal.ZERO;
        for (OrderProductParam shop : param.getShops()) {
            BigDecimal logisticsPrice = shop.getDistribution().getDistributionPrice();
            totalLogisticsPrice = totalLogisticsPrice.add(logisticsPrice);
        }
        return totalLogisticsPrice;
    }

    /**
     * 查询当前使用优惠券
     *
     * @param couponId
     * @param buyerUserId
     * @return
     */
    private CereBuyerCoupon getBuyerCoupon(Long couponId, Long buyerUserId) {
        return cereBuyerCouponService.findByCouponId(couponId, buyerUserId);
    }


    /**
     * 获取购买 sku数量
     *
     * @param param /
     * @return sku数量map
     */
    private Map<Long, ProductSku> getBuySkuMap(OrderParam param) {
        Map<Long, ProductSku> numberMap = new HashMap<>();
        param.getShops().forEach(shop -> {
            if (!EmptyUtils.isEmpty(shop.getSkus())) {
                shop.getSkus().forEach(sku -> {
                    numberMap.put(sku.getSkuId(), sku);
                });
            }
        });
        return numberMap;
    }


    /**
     * 计算订单总价格
     *
     * @param map
     * @param numberMap
     * @return
     */
    private BigDecimal calOrderPrice(Map<Long, CartSku> map, Map<Long, ProductSku> numberMap, Map<Long, BigDecimal> shopPriceMap) {
        BigDecimal total = BigDecimal.ZERO;
        for (Long skuId : map.keySet()) {
            CartSku sku = map.get(skuId);
            Long shopId = sku.getShopId();
            BigDecimal num = new BigDecimal(numberMap.get(skuId).getNumber());
            BigDecimal price = sku.getPrice().multiply(num);
            total = total.add(price);
            shopPriceMap.put(shopId, shopPriceMap.getOrDefault(shopId, BigDecimal.ZERO).add(price));
        }
        return total;
    }


    private CereOrderParent createParentOrder(BigDecimal payPrice, String time, BigDecimal logisticPrice, BigDecimal orderPrice) throws CoBusinessException {
        CereOrderParent parent = new CereOrderParent();
        parent.setCreateTime(time);
        parent.setLogisticsPrice(logisticPrice);
        //订单总价
        parent.setOrderPrice(orderPrice);
        parent.setPrice(payPrice);
        parent.setParentFormid(RandomStringUtil.getRandomCode(15, 0));
        cereOrderParentService.insert(parent);
        return parent;
    }

    /**
     * 计算优惠金额
     *
     * @param buyerCoupon
     * @param param
     * @param map
     * @param paramSkuMap
     * @param discountMap
     * @param orderPrice
     * @return
     */
    private BigDecimal setDiscountPrice(CereBuyerCoupon buyerCoupon, OrderParam param, Map<Long,
            CartSku> map, Map<Long, ProductSku> paramSkuMap, Map<Long, CereBuyerShopCoupon> discountMap,
                                        BigDecimal orderPrice, Map<Long, BigDecimal> shopPriceMap,
                                        Map<Long, BigDecimal> platformCouponDiscountMap, Map<Long, BigDecimal> shopDiscountPriceMap) throws CoBusinessException {
        //定义满足优惠券商品价格总计
        BigDecimal total;
        BigDecimal decimal = BigDecimal.ZERO;
        if (buyerCoupon != null) {
            CerePlatformActivity activity = cerePlatformActivityService.findById(buyerCoupon.getCouponId());
            if (!IntegerEnum.ACTIVITY_START.getCode().equals(activity.getState())) {
                throw new CoBusinessException(CoReturnFormat.PLATFORM_COUPON_EXPIRED);
            }
            List<Long> productIdList = cerePlatformActivityService.findProductIdList(buyerCoupon.getCouponId());
            // useBuyerCouponList = map.values.stream().map().collect
            // userBuyerCouponList.stream().set
            // 定价的筛一边分组，排前面，其余的，按照price 除以总的actualPrice的比例均摊
            List<CartSku> priceSkuList = map.values().stream().filter(obj -> obj.getPriceId() != null && obj.getPriceId() > 0).collect(Collectors.toList());
            List<Long> priceSkuIdList = priceSkuList.stream().map(CartSku::getSkuId).collect(Collectors.toList());
            Map<Long, List<CartSku>> priceSkuMap = priceSkuList.stream().collect(Collectors.groupingBy(CartSku::getPriceId));
            BigDecimal usedTotalActualPrice = BigDecimal.ZERO;
            for (Map.Entry<Long, CartSku> entry : map.entrySet()) {
                CartSku sku = entry.getValue();
                if (sku.getBuyerCouponId() != null
                        && sku.getBuyerCouponId().equals(buyerCoupon.getCouponId())
                        && productIdList.contains(sku.getProductId())) {
                    usedTotalActualPrice = usedTotalActualPrice.add(sku.getActualPrice());
                }
            }

            //整张平台优惠券可优惠多少金额
            if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(buyerCoupon.getDiscountMode())) {
                decimal = decimal.add(buyerCoupon.getReduceMoney());
            } else {
                BigDecimal discountPromote = BigDecimal.TEN.subtract(buyerCoupon.getReduceMoney()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                decimal = usedTotalActualPrice.multiply(discountPromote);
            }

            //计算每个商家 用这张平台优惠券 优惠了多少金额
            BigDecimal calculatedDiscountPrice = BigDecimal.ZERO;
            List<Map.Entry<Long, BigDecimal>> shopPriceList = new ArrayList<>(shopPriceMap.entrySet());
            for (int i = 0; i < shopPriceList.size(); i++) {
                Long shopId = shopPriceList.get(i).getKey();
                if (i == shopPriceList.size() - 1) {
                    platformCouponDiscountMap.put(shopId, decimal.subtract(calculatedDiscountPrice));
                } else {
                    BigDecimal percent = shopPriceList.get(i).getValue().divide(orderPrice, 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal shopDiscountPrice = decimal.multiply(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
                    platformCouponDiscountMap.put(shopId, shopDiscountPrice);
                    calculatedDiscountPrice = calculatedDiscountPrice.add(shopDiscountPrice);
                }
            }

            Long lastSkuId = null;
            if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(buyerCoupon.getDiscountMode())) {
                //整张优惠券是按照价格比例均摊抵扣的，每抵扣一个sku，就减少一部分
                BigDecimal remainMoney = buyerCoupon.getReduceMoney();
                for (Map.Entry<Long, List<CartSku>> entry : priceSkuMap.entrySet()) {
                    List<CartSku> skuList = entry.getValue();
                    //由于限制了定价捆绑，一个捆绑内的商品会同时满足使用优惠券，或同时不满足，所以只需要判断第一个
                    CartSku sku0 = skuList.stream().filter(obj -> !obj.getActualPrice().equals(BigDecimal.ZERO)).findAny().get();
                    if (sku0.getBuyerCouponId() != null
                            && sku0.getBuyerCouponId().equals(buyerCoupon.getCouponId())
                            && productIdList.contains(sku0.getProductId())) {
                        BigDecimal actualPrice = skuList.stream().map(CartSku::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                        //如果正好等于usedTotalActualPrice 说明只有这个定价捆绑组合使用了该优惠券
                        if (actualPrice.equals(usedTotalActualPrice)) {
                            actualPrice = sku0.getActualPrice().subtract(buyerCoupon.getReduceMoney());
                            remainMoney = BigDecimal.ZERO;
                        } else {
                            BigDecimal percent = actualPrice.divide(usedTotalActualPrice);
                            //该sku抵扣掉的优惠金额
                            BigDecimal discountMoney = buyerCoupon.getReduceMoney().multiply(percent);
                            actualPrice = actualPrice.subtract(discountMoney);
                            remainMoney = remainMoney.subtract(discountMoney);
                        }
                        map.get(sku0.getSkuId()).setActualPrice(actualPrice);
                        lastSkuId = sku0.getSkuId();
                    }
                }
                //排除掉有定价捆绑的，剩下的单独计算
                List<CartSku> notPriceSkuList = map.values().stream().filter(obj -> !priceSkuIdList.contains(obj.getSkuId())).collect(Collectors.toList());
                for (CartSku sku : notPriceSkuList) {
                    if (sku.getBuyerCouponId() != null
                            && sku.getBuyerCouponId().equals(buyerCoupon.getCouponId())
                            && productIdList.contains(sku.getProductId())) {
                        BigDecimal actualPrice = sku.getActualPrice();
                        BigDecimal percent = actualPrice.divide(usedTotalActualPrice);
                        //该sku抵扣掉的优惠金额
                        BigDecimal discountMoney = buyerCoupon.getReduceMoney().multiply(percent);
                        actualPrice = actualPrice.subtract(discountMoney);
                        map.get(sku.getSkuId()).setActualPrice(actualPrice);
                        remainMoney = remainMoney.subtract(discountMoney);
                        lastSkuId = sku.getSkuId();
                        if (remainMoney.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }
                    }
                }
                if (remainMoney.compareTo(BigDecimal.ZERO) > 0
                        && lastSkuId != null) {
                    CartSku lastSku = map.get(lastSkuId);
                    lastSku.setActualPrice(lastSku.getActualPrice().subtract(remainMoney));
                }
            } else {
                for (Map.Entry<Long, CartSku> entry : map.entrySet()) {
                    CartSku sku = entry.getValue();
                    if (sku.getBuyerCouponId() != null
                            && sku.getBuyerCouponId().equals(buyerCoupon.getCouponId())
                            && productIdList.contains(sku.getProductId())) {
                        BigDecimal discountPromote = BigDecimal.TEN.subtract(buyerCoupon.getReduceMoney()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                        sku.setActualPrice(sku.getActualPrice().multiply(discountPromote));
                    }
                }
            }
        }
        //查询所有店铺优惠券数据
        List<CereBuyerShopCoupon> list = cereBuyerShopCouponService.findByIds(param.getShops());
        //计算店铺优惠券金额总和
        if (!EmptyUtils.isEmpty(list)) {
            for (CereBuyerShopCoupon cereBuyerShopCoupon : list) {
                Long shopCouponId = cereBuyerShopCoupon.getShopCouponId();
                total = BigDecimal.ZERO;
                Long shopId = param.getShops().stream().filter(o -> o.getId() != null && o.getId().equals(cereBuyerShopCoupon.getId())).map(OrderProductParam::getShopId).findAny().orElse(0L);
                List<ProductSku> skuList = param.getShops().stream().filter(o -> o.getShopId().equals(shopId)).findFirst().get().getSkus();
                int priceCount = 0;
                for (ProductSku sku : skuList) {
                    if (sku.getPriceId() != null && sku.getPriceId() > 0) {
                        priceCount++;
                    }
                }
                List<CartSku> curShopSkuList = map.values().stream().filter(obj -> obj.getShopId().equals(shopId)).collect(Collectors.toList());

                List<CartSku> useCurCouponSkuList = curShopSkuList.stream().filter(obj -> obj.getBuyerShopCouponId() != null && obj.getBuyerShopCouponId().equals(shopCouponId)).collect(Collectors.toList());

                List<Long> ids = cereBuyerShopCouponService.findProductIds(cereBuyerShopCoupon.getShopCouponId());

                for (CartSku sku : useCurCouponSkuList) {
                    if (!ids.contains(sku.getProductId())) {
                        throw new CoBusinessException(CoReturnFormat.SHOP_COUPON_ABNORMAL);
                    }
                }

                if (IntegerEnum.COUPON_TYPE_DISCOUNT.getCode().equals(cereBuyerShopCoupon.getCouponType())) {
                    for (CartSku sku : useCurCouponSkuList) {
                        if (sku.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal discountPromote = BigDecimal.TEN.subtract(cereBuyerShopCoupon.getReduceMoney()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                            map.get(sku.getSkuId()).setActualPrice(sku.getActualPrice().multiply(discountPromote));
                        }
                    }

                    if (!EmptyUtils.isEmpty(param.getShops())) {
                        //遍历购买商品
                        if (!EmptyUtils.isEmpty(map)) {
                            int couponMatchPriceCount = 0;
                            BigDecimal couponMatchTotal = BigDecimal.ZERO;
                            for (Long key : map.keySet()) {
                                if (ids.contains(map.get(key).getProductId())) {
                                    //如果商品满足优惠券,叠加总计
                                    BigDecimal skuTotal = map.get(key).getPrice().multiply(new BigDecimal(paramSkuMap.get(key).getNumber()));
                                    total = total.add(skuTotal);
                                    if (paramSkuMap.get(key).getPriceId() != null && paramSkuMap.get(key).getPriceId() > 0) {
                                        couponMatchPriceCount++;
                                        couponMatchTotal = couponMatchTotal.add(skuTotal);
                                    }
                                }
                            }
                            // 定价捆绑的优惠金额
                            BigDecimal priceDiscount = shopDiscountPriceMap.getOrDefault(shopId, BigDecimal.ZERO);
                            if (couponMatchPriceCount >= priceCount) {
                                total = total.subtract(priceDiscount);
                            } else {
                                //不是所有的定价捆绑sku都匹配当前优惠券，扣减掉 循环中多加的部分
                                total = total.subtract(couponMatchTotal);
                            }
                            //计算折扣价格=总计-总计*折扣
                            BigDecimal price = cereBuyerShopCoupon.getReduceMoney().divide(new BigDecimal(10), 2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal discount = total.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
                            // 设置本次用券抵扣掉的金额
                            cereBuyerShopCoupon.setReduceMoney(total.subtract(discount));
                            discountMap.put(cereBuyerShopCoupon.getId(), cereBuyerShopCoupon);
                            decimal = decimal.add(total.subtract(discount));
                        }
                    }
                } else {
                    BigDecimal curCouponTotalActualPrice = useCurCouponSkuList.stream().map(CartSku::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal remainMoney = cereBuyerShopCoupon.getReduceMoney();

                    Long lastSkuId = null;
                    for (CartSku sku : useCurCouponSkuList) {
                        if (sku.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
                            //抵扣比例
                            BigDecimal percent = sku.getActualPrice().divide(curCouponTotalActualPrice);
                            //本次抵扣优惠
                            BigDecimal curReduceMoney = cereBuyerShopCoupon.getReduceMoney().multiply(percent);
                            remainMoney = remainMoney.subtract(curReduceMoney);
                            map.get(sku.getSkuId()).setActualPrice(sku.getActualPrice().subtract(curReduceMoney));
                            lastSkuId = sku.getSkuId();
                        }
                    }

                    if (remainMoney.compareTo(BigDecimal.ZERO) > 0 && lastSkuId != null) {
                        CartSku lastSku = map.get(lastSkuId);
                        lastSku.setActualPrice(lastSku.getActualPrice().subtract(remainMoney));
                    }

                    //如果是满减券直接叠加
                    discountMap.put(cereBuyerShopCoupon.getId(), cereBuyerShopCoupon);
                    decimal = decimal.add(cereBuyerShopCoupon.getReduceMoney());
                }
            }
        }
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据不同支付类型生成不同的支付url
     */
    private void generatePayUrl(OrderParam orderParam, String openId, String parentFormId, String ip, PayUrl payUrl) throws Exception {
        Integer paymentMode = orderParam.getPaymentMode();
        Integer subPaymentMode = orderParam.getSubPaymentMode();
        if (paymentMode != null && paymentMode.equals(IntegerEnum.ORDER_PAY_WX.getCode())
                && subPaymentMode != null && subPaymentMode.equals(IntegerEnum.ORDER_SUB_PAYMENT_MODE_H5.getCode())) {
            // h5支付 前端重新调用统一下单
        } else if (paymentMode != null && paymentMode.equals(IntegerEnum.ORDER_PAY_WX.getCode())
                && subPaymentMode != null && subPaymentMode.equals(IntegerEnum.ORDER_SUB_PAYMENT_MODE_XCX.getCode())) {
            // 小程序支付 前端重新调用统一下单
        } else if (paymentMode != null && Arrays.asList(IntegerEnum.ORDER_PAY_ALI.getCode(),
                IntegerEnum.ORDER_PAY_HUABEI.getCode()).contains(paymentMode)) {
            // 支付宝支付
        } else {
            //生成收款码
            String formid = parentFormId + "-" + RandomStringUtil.getRandomCode(3, 0) + "XCX";
            String code_url = wxPayService.getOrderCollectionCode(formid, payUrl.getMoney(),
                    ip, WxPayEnum.PAY_TYPE_NATIVE.getCode());
            if (!EmptyUtils.isEmpty(code_url)) {
                payUrl.setUrl(ImageUtil.genQrCodeBase64ByUrl(code_url));
            }
        }
    }

    /**
     * 新增订单商品数据
     *
     * @param skus
     * @param orderId
     * @param shopId
     * @param map
     * @param attributes
     * @throws CoBusinessException
     */
    protected void addOrderProduct(List<CartSku> skus, Long orderId, Long shopId,
                                   Map<Long, CartSku> map, List<OrderProductAttribute> attributes) throws CoBusinessException {
        if (!EmptyUtils.isEmpty(skus)) {
            for (CartSku sku : skus) {
                if (sku.getShopId().equals(shopId)) {
                    CereOrderProduct orderProduct = new CereOrderProduct();
                    BeanUtils.copyProperties(sku, orderProduct);

                    orderProduct.setOrderId(orderId);
                    orderProduct.setProductPrice(sku.getPrice());
                    setActivityInfo(orderProduct, sku);
                    //插入订单商品明细数据
                    cereOrderProductService.insert(orderProduct);
                    //更新库存数量
                    int stockNumber = map.get(sku.getSkuId()).getStockNumber();
                    sku.setStockNumber(stockNumber - sku.getNumber());
                    //根据规格id查询规格属性数据
                    if (!EmptyUtils.isEmpty(attributes)) {
                        attributes.forEach(a -> {
                            if (sku.getSkuId().equals(a.getSkuId())) {
                                a.setOrderProductId(orderProduct.getOrderProductId());
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 设置活动信息
     *
     * @param op
     * @param sku
     */
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
     * 更新平台优惠券使用状态
     *
     * @param cereBuyerCoupon
     * @param time
     * @throws CoBusinessException
     */
    private void useBuyerCoupon(CereBuyerCoupon cereBuyerCoupon, String time) throws CoBusinessException {
        //处理平台优惠券
        if (cereBuyerCoupon != null) {
            //更新平台优惠券状态为已使用
            cereBuyerCoupon.setUpdateTime(time);
            cereBuyerCoupon.setState(IntegerEnum.COUPON_USE.getCode());
            cereBuyerCouponService.updateState(cereBuyerCoupon);
            //删除redis延时任务
            stringRedisService.delete(StringEnum.COUPON_OVERDUE.getCode() + "-" + cereBuyerCoupon.getBuyerUserId()
                    + "-" + cereBuyerCoupon.getActivityId() + "-" + cereBuyerCoupon.getFullMoney());
            //删除redis记录数据
            cereRedisKeyServcice.deleteByKey(StringEnum.COUPON_OVERDUE.getCode() + "-" + cereBuyerCoupon.getBuyerUserId()
                    + "-" + cereBuyerCoupon.getActivityId() + "-" + cereBuyerCoupon.getFullMoney());
        }
    }

    private CartSku convertCartSku(ProductSku productSku) {
        CartSku skuItem = new CartSku();
        BeanUtils.copyProperties(productSku, skuItem);
        return skuItem;
    }
}
