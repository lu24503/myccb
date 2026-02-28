/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.price;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.canvas.CanvasProduct;
import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.app.param.canvas.CanvasProductParam;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.tool.CerePriceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePriceProductDAO extends BaseMapper<CerePlatformPermission> {

    int insertSelective(CerePriceProduct record);

    List<CerePriceProduct> selectProductIdList(@Param("priceIdList") List<Long> priceIdList,
                                               @Param("productIdList") List<Long> productIdList);

    List<CanvasProduct> getPriceProducts(CanvasProductParam param);

    List<ComposeProduct> findProducts(Long priceId);
}
