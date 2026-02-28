/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.cart.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.shop.cereshop.app.dao.cart.CereShopCartDAO;
import com.shop.cereshop.app.dao.product.CereProductStatsByDayDAO;
import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.cart.ShopCart;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.param.cart.*;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.cart.CereCartAttributeService;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.compose.CereComposeProductService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.discount.CereShopDiscountDetailService;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkDetailService;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.app.service.price.CerePriceRuleService;
import com.shop.cereshop.app.service.price.CereShopPriceService;
import com.shop.cereshop.app.service.product.CereProductMemberService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberService;
import com.shop.cereshop.app.service.scene.CereShopSceneService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillDetailService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.app.service.shop.CereShopConversionService;
import com.shop.cereshop.app.service.stock.CereStockService;
import com.shop.cereshop.app.utils.SceneUtil;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.ParamEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.cart.CereCartAttribute;
import com.shop.cereshop.commons.domain.cart.CereShopCart;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformDiscount;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;
import com.shop.cereshop.commons.domain.product.CereProductMember;
import com.shop.cereshop.commons.domain.product.CereProductStatsByDay;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.scene.CereShopScene;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import com.shop.cereshop.commons.domain.shop.CereShopConversion;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import com.shop.cereshop.commons.domain.tool.CereShopDiscount;
import com.shop.cereshop.commons.domain.tool.CereShopSeckill;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CereShopCartServiceImpl implements CereShopCartService {

    @Autowired
    private CereShopCartDAO cereShopCartDAO;

    @Autowired
    private CereCartAttributeService cereCartAttributeService;

    @Autowired
    private CereShopConversionService cereShopConversionService;

    @Autowired
    private CerePlatformLogService cerePlatformLogService;

    @Autowired
    private CereShopGroupWorkDetailService cereShopGroupWorkDetailService;

    @Autowired
    private CerePlatformSeckillService cerePlatformSeckillService;

    @Autowired
    private CerePlatformDiscountService cerePlatformDiscountService;

    @Autowired
    private CereShopSeckillDetailService cereShopSeckillDetailService;

    @Autowired
    private CereShopDiscountDetailService cereShopDiscountDetailService;

    @Autowired
    private CereShopSeckillService cereShopSeckillService;

    @Autowired
    private CereShopDiscountService cereShopDiscountService;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereShopPriceService cereShopPriceService;

    @Autowired
    private CerePriceRuleService cerePriceRuleService;

    @Autowired
    private CereComposeProductService cereComposeProductService;

    @Autowired
    private CereShopSceneService cereShopSceneService;

    @Autowired
    private CerePlatformMemberLevelService cerePlatformMemberLevelService;

    @Autowired
    private CereShopSceneMemberService cereShopSceneMemberService;

    @Autowired
    private CereProductMemberService cereProductMemberService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private CereShopCouponService cereShopCouponService;

    @Autowired
    private CereStockService cereStockService;

    @Autowired
    private CereProductStatsByDayDAO cereProductStatsByDayDAO;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void addCart(AddCartParam param, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        //查询活动价格
        ActivityData data=cereShopCartDAO.findActivityData(param.getSkuId());
        //查询购物车中是否已存在该商品
        CereShopCart shopCart=cereShopCartDAO.findShopCart(user.getBuyerUserId(),param.getSkuId());
        if(shopCart!=null){
            //在原有数据基础上加上数量
            shopCart.setNumber(shopCart.getNumber()+param.getNumber());
            shopCart.setUpdateTime(time);
            if(data!=null){
                //校验活动商品限量和限购
                checkActivity(data,shopCart);
                //更新活动价格
                shopCart.setProductPrice(data.getPrice());
            }
            cereShopCartDAO.updateByPrimaryKeySelective(shopCart);
            //更新日度统计
            CereProductStatsByDay stats = new CereProductStatsByDay();
            stats.setShopId(shopCart.getShopId());
            stats.setCreateDate(TimeUtils.today());
            stats.setProductId(shopCart.getProductId());
            stats.setAddCartCount(param.getNumber());
            stats.setVisitCount(0);
            stats.setSalesVolume(0);
            cereProductStatsByDayDAO.insertOrUpdate(stats);
        }else {
            //新增购物车商品数据
            shopCart=cereShopCartDAO.findSku(param.getSkuId());
            shopCart.setCreateTime(time);
            shopCart.setUpdateTime(time);
            shopCart.setBuyerUserId(user.getBuyerUserId());
            shopCart.setNumber(param.getNumber());
            if(data!=null){
                //校验活动商品限量和限购
                checkActivity(data,shopCart);
                //更新活动价格
                shopCart.setProductPrice(data.getPrice());
            }
            cereShopCartDAO.insertOrUpdate(shopCart);
            //查询规格属性值数据同步到购物车商品规格明细表中
            List<CereCartAttribute> attributes=cereShopCartDAO.findAttributes(param.getSkuId());
            if(attributes!=null){
                if (shopCart.getCartId() == null) {
                    CereShopCart cart = cereShopCartDAO.findShopCart(user.getBuyerUserId(),param.getSkuId());
                    shopCart.setCartId(cart.getCartId());
                }
                CereShopCart finalShopCart = shopCart;
                attributes.forEach(a -> a.setCartId(finalShopCart.getCartId()));
                cereCartAttributeService.insertBatch(attributes);
            }
            //更新日度统计
            CereProductStatsByDay stats = new CereProductStatsByDay();
            stats.setShopId(shopCart.getShopId());
            stats.setCreateDate(TimeUtils.today());
            stats.setProductId(shopCart.getProductId());
            stats.setAddCartCount(param.getNumber());
            stats.setVisitCount(0);
            stats.setSalesVolume(0);
            cereProductStatsByDayDAO.insertOrUpdate(stats);
        }
        //新增转化数据
        CereShopConversion cereShopConversion=new CereShopConversion();
        cereShopConversion.setShopId(shopCart.getShopId());
        cereShopConversion.setCreateTime(time);
        cereShopConversion.setType(ParamEnum.CONVERSION_CART.getCode());
        cereShopConversionService.insert(cereShopConversion);
        //新增日志
        cerePlatformLogService.addLog(user,"购物车模块","客户端操作","加入购物车",user.getBuyerUserId(),time);
    }

    private void checkActivity(ActivityData data, CereShopCart shopCart) throws CoBusinessException{
        if(!EmptyUtils.isEmpty(data.getShopSeckillId())){
            //秒杀活动校验,查询秒杀活动数据
            CereShopSeckill cereShopSeckill=cereShopSeckillService.findById(data.getShopSeckillId());
            if(cereShopSeckill!=null){
                if(IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopSeckill.getIfLimit())){
                    //如果秒杀活动限购,校验数量
                    if(shopCart.getNumber()>cereShopSeckill.getLimitNumber()){
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
                if(IntegerEnum.YES.getCode().equals(cereShopSeckill.getIfNumber())){
                    int surplusNumber = 0;
                    ProductStockInfo productStockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode(),
                            shopCart.getSkuId());
                    if (productStockInfo != null) {
                        surplusNumber = productStockInfo.getStockNumber();
                    } else {
                        surplusNumber = cereShopSeckillDetailService.findNumber(cereShopSeckill.getShopSeckillId(),shopCart.getSkuId());
                    }
                    //如果限量,查询缓存中活动商品仅剩数量
                    /*if(EmptyUtils.isEmpty(stringRedisService.get("秒杀活动商品仅剩件数"+cereShopSeckill.getShopSeckillId()+shopCart.getSkuId()))){
                        //如果没有,取数据库限量库存数据
                        surplusNumber=cereShopSeckillDetailService.findNumber(cereShopSeckill.getShopSeckillId(),shopCart.getSkuId());
                    }else {
                        //如果有,取缓存
                        surplusNumber= (int) stringRedisService.get("秒杀活动商品仅剩件数"+cereShopSeckill.getShopSeckillId()+shopCart.getSkuId());
                    }*/
                    //判断数量是否大于当前购买数量
                    if(shopCart.getNumber()>surplusNumber){
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_ALREADY_SELL_OUT);
                    }
                }
            }
        }else if (!EmptyUtils.isEmpty(data.getShopDiscountId())){
            //限时折扣活动校验,查询限时折扣活动数据
            CereShopDiscount cereShopDiscount=cereShopDiscountService.findById(data.getShopDiscountId());
            if(cereShopDiscount!=null) {
                if (IntegerEnum.PRODUCT_IF_LIMIT_YES.getCode().equals(cereShopDiscount.getIfLimit())) {
                    //如果限时折扣活动限购,校验数量
                    if (shopCart.getNumber() > cereShopDiscount.getLimitNumber()) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_LIMIT_ERROR);
                    }
                }
                if (IntegerEnum.YES.getCode().equals(cereShopDiscount.getIfNumber())) {
                    int surplusNumber = 0;
                    ProductStockInfo productStockInfo = cereStockService.getActivitySkuStock(IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode(),
                            shopCart.getSkuId());
                    if (productStockInfo != null) {
                        surplusNumber = productStockInfo.getStockNumber();
                    } else {
                        surplusNumber = cereShopDiscountDetailService.findNumber(cereShopDiscount.getShopDiscountId(),shopCart.getSkuId());
                    }
                    //如果限量,查询缓存中活动商品仅剩数量
                    /*if (EmptyUtils.isEmpty(stringRedisService.get("限时折扣活动商品仅剩件数" + cereShopDiscount.getShopDiscountId() + shopCart.getSkuId()))) {
                        //如果没有,取数据库限量库存数据
                        surplusNumber=cereShopDiscountDetailService.findNumber(cereShopDiscount.getShopDiscountId(),shopCart.getSkuId());
                    }else {
                        //如果有,取缓存
                        surplusNumber = (int) stringRedisService.get("限时折扣活动商品仅剩件数" + cereShopDiscount.getShopDiscountId() + shopCart.getSkuId());
                    }*/
                    //判断数量是否大于当前购买数量
                    if (shopCart.getNumber() > surplusNumber) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_ALREADY_SELL_OUT);
                    }
                }
            }
        }
    }

    @Override
    public List<ShopCart> getCart(CereBuyerUser user) throws CoBusinessException {
        Long buyerUserId = user.getBuyerUserId();
        //查询当前用户购物车店铺数据
        List<ShopCart> shopCarts=cereShopCartDAO.findCartByUserId(buyerUserId);
        if(!EmptyUtils.isEmpty(shopCarts)){
            //查询商品明细数据
            shopCarts.forEach(shopCart -> {
                //根据店铺id查询商品明细
                List<CartSku> cartSkus = cereShopCartDAO.findProductByShopId(shopCart.getShopId(),buyerUserId);
                Map<Long, Map<String, Sku>> productIdSkuMap = new HashMap<>();
                if(!EmptyUtils.isEmpty(cartSkus)){
                    cartSkus.forEach(a -> a.setShopId(shopCart.getShopId()));
                    cartSkus.forEach(a -> a.setValues(EmptyUtils.getImages(a.getValue())));
                    cartSkus.forEach(this::setActivityInfo);
                    setShopActivityInfo(cartSkus, user);
                    Map<Long, List<ProductCoupon>> platformCouponMap = new HashMap<>();
                    Map<Long, List<ProductCoupon>> shopCouponMap = new HashMap<>();
                    for (CartSku sku : cartSkus) {
                        Map<String, Sku> skuMap = productIdSkuMap.get(sku.getProductId());
                        if (skuMap == null) {
                            skuMap = getSkuMap(sku.getProductId());
                            sku.setMap(skuMap);
                            productIdSkuMap.put(sku.getProductId(), skuMap);
                        }
                        setPlatformCoupons(user, sku, platformCouponMap);
                        setShopCoupons(user, sku, shopCouponMap);
                    }
                    shopCart.setSkus(cartSkus);
                }
            });
        }
        return shopCarts;
    }

    private Map<String, Sku> getSkuMap(Long productId) {
        //查询该商品所有组合规格数据封装到map
        List<Sku> skus=cereProductSkuService.findSkuByProductId(productId);
        if(!EmptyUtils.isEmpty(skus)){
            Map<String, Sku> map = new HashMap<>();
            skus.forEach(sku -> {
                //查询规格所有规格值拼接字符串
                String key = cereProductSkuService.findValueBySkuId(sku.getSkuId());
                if(EmptyUtils.isEmpty(key)){
                    key="单款项";
                }
                map.put(key, sku);
            });
            return map;
        }
        return new HashMap<>();
    }

    /**
     * 设置活动信息
     * @param sku
     */
    private void setActivityInfo(CartSku sku) {

        BigDecimal num = new BigDecimal(sku.getNumber());

        //拼团
        if (sku.getActivityType() != null && IntegerEnum.ACTIVITY_TYPE_SHOP_GROUP.getCode().equals(sku.getActivityType())) {
            ActivityData data = cereShopGroupWorkDetailService.findPriceBySkuId(sku.getSkuId());
            if (data != null) {
                sku.setPrice(data.getPrice());
                sku.setTotal(data.getPrice().multiply(num));
            }
        }

        //平台秒杀
        if (sku.getActivityType() != null && IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(sku.getActivityType())) {
            CerePlatformSeckill platformSeckill = cerePlatformSeckillService.findById(sku.getPlatformSeckillId());
            if (platformSeckill != null && IntegerEnum.ACTIVITY_START.getCode().equals(platformSeckill.getState())) {
                sku.setPrice(sku.getOriginalPrice().subtract(platformSeckill.getSeckillMoney()));
                sku.setTotal(sku.getPrice().multiply(num));
            }
        }

        //平台折扣
        if (sku.getActivityType() != null && IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(sku.getActivityType())) {
            CerePlatformDiscount platformDiscount = cerePlatformDiscountService.findById(sku.getPlatformDiscountId());
            if (platformDiscount != null && IntegerEnum.ACTIVITY_START.getCode().equals(platformDiscount.getState())) {
                sku.setPrice(sku.getOriginalPrice().multiply(platformDiscount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                sku.setTotal(sku.getPrice().multiply(num));
            }
        }

        //商家秒杀
        if (sku.getActivityType() != null && IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(sku.getActivityType())) {
            CereShopSeckill cereShopSeckill = cereShopSeckillService.findById(sku.getShopSeckillId());
            if (cereShopSeckill != null && IntegerEnum.TOOL_HAND.getCode().equals(cereShopSeckill.getState())) {
                ActivityData data = cereShopSeckillDetailService.findPriceBySkuId(sku.getSkuId());
                if (data != null) {
                    sku.setPrice(data.getPrice());
                    sku.setTotal(sku.getPrice().multiply(num));
                }
            }
        }

        //商家折扣
        if (sku.getActivityType() != null && IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(sku.getActivityType())) {
            CereShopDiscount cereShopDiscount = cereShopDiscountService.findById(sku.getShopDiscountId());
            if (cereShopDiscount != null && IntegerEnum.TOOL_HAND.getCode().equals(cereShopDiscount.getState())) {
                ActivityData data = cereShopDiscountDetailService.findPriceBySkuId(sku.getSkuId());
                if (data != null) {
                    sku.setPrice(data.getPrice());
                    sku.setTotal(sku.getPrice().multiply(num));
                }
            }
        }
    }

    /**
     * 设置需要组合的活动信息
     * @param cartSkus
     */
    private void setShopActivityInfo(List<CartSku> cartSkus, CereBuyerUser user) {
        //定价捆绑
        List<CartSku> skus = cartSkus.stream().filter(cartSku -> cartSku.getPriceId() != null && cartSku.getPriceId() > 0
                && IntegerEnum.YES.getCode().equals(cartSku.getShelveState())).collect(Collectors.toList());
        Map<Long, List<CartSku>> priceIdMap = skus.stream().collect(Collectors.groupingBy(CartSku::getPriceId));
        for (Map.Entry<Long, List<CartSku>> entry:priceIdMap.entrySet()) {
            Long priceId = entry.getKey();
            List<CerePriceRule> ruleList = cerePriceRuleService.findRules(priceId);
            //按数量倒序
            Collections.reverse(ruleList);

            List<CartSku> priceSkuList = entry.getValue();
            priceSkuList.sort(new Comparator<CartSku>() {
                @Override
                public int compare(CartSku o1, CartSku o2) {
                    return o2.getNumber().compareTo(o1.getNumber());
                }
            });

            int priceTotalNum = priceSkuList.stream().mapToInt(CartSku::getNumber).sum();
            for (CerePriceRule rule:ruleList) {
                if (priceTotalNum >= rule.getNumber()) {
                    // 可以优惠多少次
                    int multiple = priceTotalNum/rule.getNumber();
                    int calNum = 0;
                    for (CartSku sku:priceSkuList) {
                        sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_PRICE.getCode());
                        calNum += sku.getNumber();
                        //说明达到匹配条件了
                        if (calNum >= multiple * rule.getNumber()) {
                            break;
                        }
                    }
                    break;
                }
            }
        }

        //组合捆绑 先筛选出没有参加活动，再根据商品id查询组合捆绑信息
        skus = cartSkus.stream().filter(cartSku -> (cartSku.getActivityType() == null || cartSku.getActivityType().equals(IntegerEnum.ACTIVITY_TYPE_NORMAL.getCode()))
                    && IntegerEnum.YES.getCode().equals(cartSku.getShelveState())).collect(Collectors.toList());
        Map<Long, List<Long>> composeIdMap = cereComposeProductService.selectComposeIdListByProductIdList(skus.stream().map(CartSku::getProductId).collect(Collectors.toList()));
        for (CartSku sku:skus) {
            List<Long> composeIdList = composeIdMap.get(sku.getProductId());
            if (composeIdList != null) {
                sku.setComposeIdList(composeIdList);
                sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_COMPOSE.getCode());
            }
        }
        /*Map<Long, List<CartSku>> composeIdMap = skus.stream().collect(Collectors.groupingBy(CartSku::getComposeId));
        for (Map.Entry<Long, List<CartSku>> entry:composeIdMap.entrySet()) {
            Long composeId = entry.getKey();
            List<Long> productIdList = cereComposeProductService.selectByComposeId(composeId);
            List<Long> skuProductIdList = entry.getValue().stream().map(CartSku::getProductId).collect(Collectors.toList());
            List<Long> interProductIdList = (List<Long>) CollectionUtil.intersection(productIdList, skuProductIdList);
            // 取交集之后，和组合活动的商品数相同，说明符合条件
            if (interProductIdList.size() == productIdList.size()) {
                Map<Long, Integer> productIdBuyNumMap = new HashMap<>();
                for (CartSku sku:entry.getValue()) {
                    int buyNum = productIdBuyNumMap.getOrDefault(sku.getProductId(), 0);
                    productIdBuyNumMap.put(sku.getProductId(), buyNum + sku.getNumber());
                    sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_COMPOSE.getCode());
                }
                //最小购买数量代表 组合次数
                int minSize = Collections.min(productIdBuyNumMap.values());
            }
        }*/

        //场景营销
        skus = cartSkus.stream()
                .filter(cartSku -> cartSku.getActivityType() == null || IntegerEnum.ACTIVITY_TYPE_NORMAL.getCode().equals(cartSku.getActivityType()))
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(skus)) {
            setSceneInfo(skus, user);
        }

        //会员价
        skus = cartSkus.stream()
                .filter(cartSku -> cartSku.getActivityType() == null || IntegerEnum.ACTIVITY_TYPE_NORMAL.getCode().equals(cartSku.getActivityType()))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(skus)) {
            setMemberInfo(skus, user);
        }
    }

    /**
     * 设置场景营销信息
     * @param skus
     * @param user
     */
    private void setSceneInfo(List<CartSku> skus, CereBuyerUser user) {
        List<CereShopScene> sceneList = cereShopSceneService.selectOnGoingMarketingByShopId(skus.get(0).getShopId());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(sceneList)) {
            Long memberLevelId = null;
            if (user != null) {
                memberLevelId = user.getMemberLevelId();
            }
            //没登录的情况下默认第一等级会员
            if (memberLevelId == null) {
                CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
                if (firstLevel != null) {
                    memberLevelId = firstLevel.getMemberLevelId();
                }
            }
            // 如果还没有初始化会员等级信息，直接返回商品详情
            if (memberLevelId == null) {
                return;
            }
            for (CereShopScene scene:sceneList) {
                CereShopSceneMember sceneMember = cereShopSceneMemberService.selectSceneMemberList(scene.getSceneId(), memberLevelId);
                //如果数据没维护好直接返回商品
                if (sceneMember == null) {
                    return;
                }
                BigDecimal discount = sceneMember.getDiscount();

                boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), user != null ? user.getBirthday() : null, false);
                if (matched) {
                    for (CartSku sku:skus) {
                        sku.setSceneId(scene.getSceneId());
                        sku.setPrice(sku.getPrice().multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                        sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_SCENE.getCode());
                    }
                }
                break;
            }
        }
    }

    /**
     * 设置会员价信息
     * @param skus
     * @param user
     */
    private void setMemberInfo(List<CartSku> skus, CereBuyerUser user) {
        Long memberLevelId = null;
        if (user != null && user.getMemberLevelId() != null) {
            memberLevelId = user.getMemberLevelId();
        }
        if (memberLevelId == null) {
            CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
            if (firstLevel != null) {
                memberLevelId = firstLevel.getMemberLevelId();
            }
        }
        // 如果还没有初始化会员等级信息 或者sku信息为空，直接返回商品详情
        if (memberLevelId == null) {
            return;
        }
        for (CartSku sku:skus) {
            CereProductMember productMember = cereProductMemberService.selectProductMember(memberLevelId, sku.getProductId(), sku.getSkuId());
            if (productMember != null) {
                sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_MEMBER.getCode());
                int mode = productMember.getMode();
                BigDecimal price = productMember.getPrice();
                if (IntegerEnum.MEMBER_PRODUCT_MODE_DISCOUNT.getCode().equals(mode)) {
                    sku.setPrice(sku.getPrice().multiply(price).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    sku.setPrice(price);
                }
            }
        }
    }

    /**
     * 设置平台优惠券
     * @param user
     * @param sku
     * @param couponMap
     */
    private void setPlatformCoupons(CereBuyerUser user, CartSku sku, Map<Long, List<ProductCoupon>> couponMap) {
        if (sku.getIfCouponAdd() != null && IntegerEnum.NO.getCode().equals(sku.getIfCouponAdd())) {
            return;
        }
        List<ProductCoupon> couponList = couponMap.get(sku.getProductId());
        if (couponList == null) {
            if(user!=null){
                //过滤已使用和已过期的优惠券
                couponList=cerePlatformActivityService.findCouponByProductIdAndUserId(user.getBuyerUserId(),sku.getProductId());
            } else {
                couponList=cerePlatformActivityService.findCouponByProductId(sku.getProductId());
            }
            couponMap.put(sku.getProductId(), couponList);
        }
        sku.setMarkTools(couponList);
    }

    /**
     * 设置商家优惠券
     * @param user
     * @param sku
     * @param couponMap
     */
    private void setShopCoupons(CereBuyerUser user, CartSku sku, Map<Long, List<ProductCoupon>> couponMap) {
        if (sku.getIfCouponAdd() != null && IntegerEnum.NO.getCode().equals(sku.getIfCouponAdd())) {
            return;
        }
        List<ProductCoupon> couponList = couponMap.get(sku.getProductId());
        if (couponList == null) {
            if(user!=null){
                couponList=cereShopCouponService.findByProductIdAndUserId(user.getBuyerUserId(), sku.getShopId(), sku.getProductId());
            }else {
                couponList=cereShopCouponService.findByProductId(sku.getShopId(), sku.getProductId());
            }
            couponMap.put(sku.getProductId(), couponList);
        }
        sku.setShopMarkTools(couponList);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void delete(DeleteParam param, CereBuyerUser user) throws CoBusinessException {
        if(!EmptyUtils.isEmpty(param.getIds())){
            cereShopCartDAO.deleteByIds(param.getIds(),user.getBuyerUserId());
        }
    }

    @Override
    public void deleteAll(Long buyerUserId) throws CoBusinessException {
        cereShopCartDAO.deleteAll(buyerUserId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void selected(SelectedParam param,CereBuyerUser user) throws CoBusinessException {
        /*List<Long> select=new ArrayList<>();
        List<Long> unSelect=new ArrayList<>();
        //更新购物车选中状态
        if(!EmptyUtils.isEmpty(param.getShopCarts())){
            param.getShopCarts().forEach(shopCart -> {
                if(!EmptyUtils.isEmpty(shopCart.getSkus())){
                    shopCart.getSkus().forEach(sku -> {
                        if(IntegerEnum.YES.getCode().equals(sku.getSelected())){
                            select.add(sku.getSkuId());
                        }else {
                            unSelect.add(sku.getSkuId());
                        }
                    });
                }
            });
            if(!EmptyUtils.isEmpty(select)){
                //更新选中规格
                cereShopCartDAO.updateSelected(select,user.getBuyerUserId(),IntegerEnum.YES.getCode());
            }
            if(!EmptyUtils.isEmpty(unSelect)){
                //更新未选中规格
                cereShopCartDAO.updateSelected(unSelect,user.getBuyerUserId(),IntegerEnum.NO.getCode());
            }
        }else {
            //全选
            cereShopCartDAO.updateSelectedAll(user.getBuyerUserId());
        }*/
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void updateNumber(CartUpdateParam param, CereBuyerUser user) throws CoBusinessException {
        String time =TimeUtils.yyMMddHHmmss();
        //查询活动价格
        ActivityData data=cereShopCartDAO.findActivityData(param.getSkuId());
        //查询购物车商品数据
        CereShopCart shopCart=cereShopCartDAO.findBySkuIdAndUser(param.getSkuId(),user.getBuyerUserId());
        if(shopCart!=null){
            shopCart.setNumber(param.getNumber());
            shopCart.setUpdateTime(time);
            if(data!=null){
                //校验活动限量和限购
                checkActivity(data,shopCart);
            }
            cereShopCartDAO.updateByPrimaryKeySelective(shopCart);
        }
    }

    @Override
    public void deleteSkus(List<CartSku> carts, Long buyerUserId) {
        cereShopCartDAO.deleteSkus(carts,buyerUserId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void addBatchCart(BatchCartParam param, CereBuyerUser user) throws CoBusinessException {
        String time =TimeUtils.yyMMddHHmmss();
        if(!EmptyUtils.isEmpty(param.getCarts())){
            for (AddCartParam cart:param.getCarts()) {
                //查询购物车中是否已存在该商品
                CereShopCart shopCart=cereShopCartDAO.findShopCart(user.getBuyerUserId(),cart.getSkuId());
                if(shopCart!=null){
                    //在原有数据基础上加上数量
                    shopCart.setNumber(shopCart.getNumber()+cart.getNumber());
                    shopCart.setUpdateTime(time);
                    cereShopCartDAO.updateByPrimaryKeySelective(shopCart);
                }else {
                    //新增购物车商品数据
                    shopCart=cereShopCartDAO.findSku(cart.getSkuId());
                    shopCart.setCreateTime(time);
                    shopCart.setBuyerUserId(user.getBuyerUserId());
                    shopCart.setNumber(cart.getNumber());
                    cereShopCartDAO.insert(shopCart);
                    //查询规格属性值数据同步到购物车商品规格明细表中
                    List<CereCartAttribute> attributes=cereShopCartDAO.findAttributes(cart.getSkuId());
                    if(attributes!=null){
                        CereShopCart finalShopCart = shopCart;
                        attributes.forEach(a -> a.setCartId(finalShopCart.getCartId()));
                        cereCartAttributeService.insertBatch(attributes);
                    }
                }
                //新增转化数据
                CereShopConversion cereShopConversion=new CereShopConversion();
                cereShopConversion.setShopId(shopCart.getShopId());
                cereShopConversion.setCreateTime(time);
                cereShopConversion.setType(ParamEnum.CONVERSION_CART.getCode());
                cereShopConversionService.insert(cereShopConversion);
            }
        }
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void buyAgain(BuyerAgainParam param, CereBuyerUser user) throws CoBusinessException {
        String time=TimeUtils.yyMMddHHmmss();
        //根据订单id查询订单商品数据
        List<CartSku> skus=cereShopCartDAO.findOrderProduct(param.getOrderId());
        if(!EmptyUtils.isEmpty(skus)){
            List<CereShopCart> updates=new ArrayList<>();
            List<CereShopCart> adds=new ArrayList<>();
            skus.forEach(sku -> {
                //查询购物车是否有该商品
                CereShopCart shopCart=cereShopCartDAO.findShopCart(user.getBuyerUserId(),sku.getSkuId());
                if(shopCart!=null){
                    //加上该订单商品数量
                    shopCart.setNumber(shopCart.getNumber()+sku.getNumber());
                    shopCart.setUpdateTime(time);
                    shopCart.setSelected(IntegerEnum.YES.getCode());
                    updates.add(shopCart);
                }else {
                    //新增购物车商品数据
                    shopCart=new CereShopCart();
                    shopCart.setCreateTime(time);
                    shopCart.setBuyerUserId(user.getBuyerUserId());
                    shopCart.setNumber(sku.getNumber());
                    shopCart.setProductId(sku.getProductId());
                    shopCart.setSkuId(sku.getSkuId());
                    shopCart.setSKU(sku.getSKU());
                    shopCart.setImage(sku.getImage());
                    shopCart.setProductPrice(sku.getPrice());
                    shopCart.setWeight(sku.getWeight());
                    shopCart.setSelected(IntegerEnum.NO.getCode());
                    shopCart.setProductName(sku.getProductName());
                    shopCart.setShopId(sku.getShopId());
                    shopCart.setSelected(IntegerEnum.YES.getCode());
                    adds.add(shopCart);
                }
            });
            if(!EmptyUtils.isEmpty(adds)){
                cereShopCartDAO.insertBatch(adds);
            }
            if(!EmptyUtils.isEmpty(updates)){
                cereShopCartDAO.updateBatch(updates);
            }
        }
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void updateSku(UpdateSkuParam param, CereBuyerUser user) throws CoBusinessException {
        //根据原来的规格id查询购物车商品数据
        CereShopCart cereShopCart=cereShopCartDAO.findShopCart(user.getBuyerUserId(),param.getSkuId());
        if(cereShopCart!=null){
            //查询修改后的商品数据
            CereShopCart sku = cereShopCartDAO.findSku(param.getNewSkuId());
            if(sku!=null){
                cereShopCart.setSkuId(param.getNewSkuId());
                cereShopCart.setProductId(sku.getProductId());
                cereShopCart.setProductName(sku.getProductName());
                cereShopCart.setProductPrice(sku.getProductPrice());
                cereShopCart.setWeight(sku.getWeight());
                cereShopCart.setSKU(sku.getSKU());
                cereShopCart.setImage(sku.getImage());
                cereShopCart.setNumber(param.getNumber());
                //查询活动价格
                ActivityData data=cereShopGroupWorkDetailService.findPriceBySkuId(param.getNewSkuId());
                if(data==null){
                    //查询秒杀价格
                    data=cereShopSeckillDetailService.findPriceBySkuId(param.getNewSkuId());
                    if(data==null){
                        //查询限时折扣价
                        data=cereShopDiscountDetailService.findPriceBySkuId(param.getNewSkuId());
                    }
                }
                if(data!=null){
                    //校验活动限购和限量
                    checkActivity(data,cereShopCart);
                }
                cereShopCartDAO.updateByPrimaryKeySelective(cereShopCart);
            }
        }
    }

    @Override
    public Integer findNumber(Long buyerUserId) {
        return cereShopCartDAO.findNumber(buyerUserId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereShopCartDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public Boolean clearInvalidSku(Long buyerUserId) {
        return cereShopCartDAO.clearInvalidSku(buyerUserId);
    }
}
