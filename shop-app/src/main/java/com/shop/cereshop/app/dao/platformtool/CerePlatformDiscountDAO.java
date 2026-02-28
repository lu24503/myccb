/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.platformtool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.canvas.CanvasPlatformDiscount;
import com.shop.cereshop.app.page.discount.PlatformDiscountProduct;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.discount.PlatformDiscountParam;
import com.shop.cereshop.app.param.discount.DiscountRelateProductInfo;
import com.shop.cereshop.app.param.discount.ShopPlatformDiscount;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformDiscount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePlatformDiscountDAO extends BaseMapper<CerePlatformDiscount> {
    int deleteByPrimaryKey(Long discountId);

    int insertSelective(CerePlatformDiscount record);

    CerePlatformDiscount selectByPrimaryKey(Long discountId);

    int updateByPrimaryKeySelective(CerePlatformDiscount record);

    int updateByPrimaryKey(CerePlatformDiscount record);

    List<PlatformDiscountProduct> queryPlatformDiscountProductList(PlatformDiscountParam param);

    List<CanvasPlatformDiscount> getMinDiscount(RenovationParam param);

    List<ToolProduct> findDistinctProducts(Long discountId);

    List<ShopPlatformDiscount> selectByShopIdList(List<Long> shopIdList);

    ProductStockInfo selectProductStockInfo(@Param("platformDiscountId") Long platformDiscountId, @Param("productId") Long productId);

    int rollbackStock(@Param("discountId") Long discountId, @Param("productId") Long productId,
                      @Param("buyNumber") Integer buyNumber);

    DiscountRelateProductInfo selectRelateInfoByProductId(Long productId);

    List<Long> queryProductIdListByDiscountId(Long discountId);
}
