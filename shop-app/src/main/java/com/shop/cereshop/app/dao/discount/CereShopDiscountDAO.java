/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.discount;

import com.shop.cereshop.app.page.discount.DiscountIndex;
import com.shop.cereshop.app.page.discount.ShopDiscountDetail;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.discount.DiscountParam;
import com.shop.cereshop.app.param.discount.DiscountRelateProductInfo;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessDiscount;
import com.shop.cereshop.commons.domain.tool.CereShopDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopDiscountDAO extends BaseMapper<CereShopDiscount> {
    int deleteByPrimaryKey(Long shopDiscountId);

    int insertSelective(CereShopDiscount record);

    CereShopDiscount selectByPrimaryKey(Long shopDiscountId);

    int updateByPrimaryKeySelective(CereShopDiscount record);

    int updateByPrimaryKey(CereShopDiscount record);

    DiscountIndex findShop(@Param("shopId") Long shopId);

    List<ToolProduct> findProducts(DiscountParam param);

    ProductDetail findBySkuId(@Param("skuId") Long skuId, @Param("shopDiscountId") Long shopDiscountId);

    Long findByProductId(@Param("skuId") Long skuId);

    List<ShopDiscountDetail> getDiscounts(RenovationParam param);

    List<ShopBusinessDiscount> selectByShopIdList(List<Long> shopIdList);

    CereShopDiscount selectByProductId(Long productId);

    DiscountRelateProductInfo selectRelateInfoBySkuId(Long skuId);
}
