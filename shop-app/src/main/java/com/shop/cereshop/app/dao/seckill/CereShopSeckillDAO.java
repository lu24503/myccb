/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.seckill;

import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.seckill.SeckillkIndex;
import com.shop.cereshop.app.page.seckill.ShopSeckillDetail;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.param.seckill.SeckillParam;
import com.shop.cereshop.app.param.seckill.SeckillRelateProductInfo;
import com.shop.cereshop.app.param.seckill.ShopBusinessSeckill;
import com.shop.cereshop.commons.domain.tool.CereShopSeckill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopSeckillDAO extends BaseMapper<CereShopSeckill> {
    int deleteByPrimaryKey(Long shopSeckillId);

    int insertSelective(CereShopSeckill record);

    CereShopSeckill selectByPrimaryKey(Long shopSeckillId);

    int updateByPrimaryKeySelective(CereShopSeckill record);

    int updateByPrimaryKey(CereShopSeckill record);

    SeckillkIndex findShop(@Param("shopId") Long shopId);

    List<ToolProduct> findProducts(SeckillParam param);

    ProductDetail findBySkuId(@Param("skuId") Long skuId, @Param("shopSeckillId") Long shopSeckillId);

    Long findSeckillIdBySkuId(@Param("skuId") Long skuId);

    List<ShopSeckillDetail> getSeckills(RenovationParam param);

    List<ShopBusinessSeckill> selectByShopIdList(List<Long> shopIdList);

    CereShopSeckill selectByProductId(Long productId);

    SeckillRelateProductInfo selectRelateInfoBySkuId(Long skuId);
}
