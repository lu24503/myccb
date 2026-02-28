/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.discount;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.tool.CereShopDiscountDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereShopDiscountDetailDAO extends BaseMapper<CereShopDiscountDetail> {

    int insertSelective(CereShopDiscountDetail record);

    CereShopDiscountDetail findSkuDetail(@Param("shopDiscountId") Long shopDiscountId,@Param("orderId") Long orderId);

    int updateNumber(CereShopDiscountDetail detail);

    ActivityData findPriceBySkuId(@Param("skuId") Long skuId);

    List<ToolProduct> findDistinctProducts(@Param("shopDiscountId") Long shopDiscountId);

    int findNumber(@Param("shopDiscountId") Long shopDiscountId,@Param("skuId") Long skuId);

    void updateBatch(@Param("list") List<CereShopDiscountDetail> list);

    List<CereShopDiscountDetail> findNumberDetails(@Param("orderId") Long orderId,@Param("shopDiscountId") Long shopDiscountId);

    ProductStockInfo selectSkuStockInfo(@Param("shopDiscountId") Long shopDiscountId, @Param("skuId") Long skuId);

    BigDecimal findPriceByDiscountIdAndSkuId(@Param("shopDiscountId") Long shopDiscountId, @Param("skuId") Long skuId);

    Long findSkuIdByProductId(Long productId);

    int rollbackStock(@Param("discountId") Long discountId, @Param("skuId") Long skuId,
                      @Param("buyNumber") Integer buyNumber);
}
