/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.page.banner.ShopBanner;
import com.shop.cereshop.app.page.index.RecommendShop;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.shop.PlatformShop;
import com.shop.cereshop.app.page.shop.Shop;
import com.shop.cereshop.app.page.shop.ShopClassify;
import com.shop.cereshop.app.page.shop.ShopIndex;
import com.shop.cereshop.app.param.shop.ShopParam;
import com.shop.cereshop.app.param.shop.ShopPosterParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CerePlatformShopservice {
    List<RecommendShop> findRecommendShop();

    RecommendShop findVolumeProductByShopId(Long shopId);

    List<RecommendShop> findRecommendProducts();

    Integer findPayUsers(Long productId);

    Shop getShopProducts(ShopParam param, CereBuyerUser user) throws CoBusinessException;

    List<ShopClassify> getShopClassify(ShopParam param) throws CoBusinessException;

    Page getExtensionProduct(ShopParam param) throws CoBusinessException;

    List<ProductCoupon> findShopToolByProductId(Long shopId, Long productId);

    List<ShopBanner> getShopBanner(Long shopId) throws CoBusinessException;

    Page getShops(ShopParam param, CereBuyerUser user) throws CoBusinessException;

    void updateState(CerePlatformShop cerePlatformShop) throws CoBusinessException;

    void insert(CerePlatformShop cerePlatformShop) throws CoBusinessException;

    void update(CerePlatformShop cerePlatformShop) throws CoBusinessException;

    PlatformShop getById(Long shopId) throws CoBusinessException;

    CerePlatformShop findByShopName(String shopName);

    CerePlatformShop findByPhone(String shopPhone);

    CerePlatformShop checkShopIdByName(String shopName, Long shopId);

    CerePlatformShop checkShopIdByPhone(String shopPhone, Long shopId);

    CerePlatformShop check(String shopPhone);

    ShopIndex getIndex(ShopParam param,CereBuyerUser user) throws CoBusinessException,Exception;

    String getSharePic(ShopPosterParam param, CereBuyerUser user) throws CoBusinessException;
}
