/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.platformtool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.canvas.CanvasPlatformSeckill;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.seckill.PlatformSeckillProduct;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.seckill.PlatformSeckillParam;
import com.shop.cereshop.app.param.seckill.SeckillRelateProductInfo;
import com.shop.cereshop.app.param.seckill.ShopPlatformSeckill;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePlatformSeckillDAO extends BaseMapper<CerePlatformSeckill> {
    int deleteByPrimaryKey(Long seckillId);

    int insertSelective(CerePlatformSeckill record);

    CerePlatformSeckill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(CerePlatformSeckill record);

    int updateByPrimaryKey(CerePlatformSeckill record);

    List<String> queryTodaySession();

    List<PlatformSeckillProduct> queryProductListBySession(PlatformSeckillParam platformSeckillParam);

    List<CanvasPlatformSeckill> getPlatformSeckills(RenovationParam param);

    List<ToolProduct> findSeckillProduct(Long seckillId);

    List<Long> queryProductIdListBySeckillId(Long seckillId);

    List<ShopPlatformSeckill> selectByShopIdList(List<Long> shopIdList);

    ProductStockInfo selectProductStockInfo(@Param("platformSeckillId") Long platformSeckillId, @Param("productId") Long productId);

    int rollbackStock(@Param("seckillId") Long seckillId, @Param("productId") Long productId,
                      @Param("buyNumber") Integer buyNumber);

    List<CerePlatformSeckill> queryPlatformSeckillList();

    SeckillRelateProductInfo selectRelateInfoByProductId(Long productId);
}
