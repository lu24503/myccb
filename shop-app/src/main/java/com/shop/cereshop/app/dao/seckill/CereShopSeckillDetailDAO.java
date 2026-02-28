/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.seckill;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.tool.CereShopSeckillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereShopSeckillDetailDAO extends BaseMapper<CereShopSeckillDetail> {

    int insertSelective(CereShopSeckillDetail record);

    CereShopSeckillDetail findSkuDetail(@Param("shopSeckillId") Long shopSeckillId,@Param("orderId") Long orderId);

    int updateNumber(CereShopSeckillDetail detail);

    ActivityData findPriceBySkuId(@Param("skuId") Long skuId);

    List<ToolProduct> findDistinctProducts(@Param("shopSeckillId") Long shopSeckillId);

    int findNumber(@Param("shopSeckillId") Long shopSeckillId,@Param("skuId") Long skuId);

    void updateBatch(@Param("list") List<CereShopSeckillDetail> list);

    List<CereShopSeckillDetail> findNumberDetails(@Param("orderId") Long orderId,@Param("shopSeckillId") Long shopSeckillId);

    ProductStockInfo selectSkuStockInfo(@Param("shopSeckillId") Long shopSeckillId, @Param("skuId") Long skuId);

    BigDecimal findPriceBySeckillIdAndSkuId(@Param("shopSeckillId") Long shopSeckillId, @Param("skuId") Long skuId);

    Long findSkuIdByProductId(Long productId);

    int rollbackStock(@Param("seckillId") Long seckillId, @Param("skuId") Long skuId,
                      @Param("buyNumber") Integer buyNumber);
}
