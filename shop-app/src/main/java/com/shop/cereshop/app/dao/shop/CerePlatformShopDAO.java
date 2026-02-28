/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.shop;

import com.shop.cereshop.app.page.banner.ShopBanner;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.index.RecommendShop;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.page.shop.*;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.shop.ShopParam;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CerePlatformShopDAO extends BaseMapper<CerePlatformShop> {
    int deleteByPrimaryKey(Long shopId);

    int insertSelective(CerePlatformShop record);

    CerePlatformShop selectByPrimaryKey(Long shopId);

    int updateByPrimaryKeySelective(CerePlatformShop record);

    int updateByPrimaryKey(CerePlatformShop record);

    List<RecommendShop> findRecommendShop();

    RecommendShop findVolumeProductByShopId(@Param("shopId") Long shopId);

    List<RecommendShop> findRecommendProducts();

    Integer findPayUsers(@Param("productId") Long productId);

    List<Product> getShopProducts(ShopParam param);

    Shop findNumber(@Param("shopId") Long shopId);

    List<ShopClassify> getShopClassify(@Param("shopId") Long shopId);

    List<Product> getExtensionProduct(ShopParam param);

    List<ProductCoupon> findShopToolByProductId(@Param("shopId") Long shopId, @Param("productId") Long productId);

    List<ShopBanner> getShopBanner(@Param("shopId") Long shopId);

    List<SettlementShop> getShops(ShopParam param);

    /** 每个店铺默认查10个商品 */
    List<CartSku> findSkuByShopParam(ShopParam param);

    void updateState(CerePlatformShop cerePlatformShop);

    PlatformShop findShop(@Param("shopId") Long shopId);

    CerePlatformShop findByShopName(@Param("shopName") String shopName);

    CerePlatformShop findByPhone(@Param("shopPhone") String shopPhone);

    CerePlatformShop checkShopIdByName(@Param("shopName") String shopName,@Param("shopId") Long shopId);

    CerePlatformShop checkShopIdByPhone(@Param("shopPhone") String shopPhone,@Param("shopId") Long shopId);

    CerePlatformShop check(@Param("shopPhone") String shopPhone);

    ShopIndex findShopIndex(@Param("shopId") Long shopId);

    List<ShopCoupon> findShopCoupons(@Param("shopId") Long shopId);

    List<ShopGroupWork> findGroupWork(@Param("shopId") Long shopId);

    List<ToolProduct> findGroupProducts(@Param("shopGroupWorkId") Long shopGroupWorkId);

    List<ShopSeckill> findSeckill(@Param("shopId") Long shopId);

    List<ToolProduct> findSeckillProducts(@Param("shopSeckillId") Long shopSeckillId);

    List<ShopDiscount> findDiscount(@Param("shopId") Long shopId);

    List<ToolProduct> findDiscountProducts(@Param("shopDiscountId") Long shopDiscountId);

    Integer findShopNumber(@Param("shopId") Long shopId);

    @MapKey("shopId")
    Map<Long, Shop> getClaasifyShop(List<Product> list);

}
