/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.price;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.price.ShopPriceDetail;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.price.ShopPricePageParam;
import com.shop.cereshop.app.param.price.ShopPriceWithRule;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.tool.CereShopPrice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereShopPriceDAO extends BaseMapper<CerePlatformPermission> {
    int deleteByPrimaryKey(Long priceId);

    int insertSelective(CereShopPrice record);

    CereShopPrice selectByPrimaryKey(Long priceId);

    int updateByPrimaryKeySelective(CereShopPrice record);

    int updateByPrimaryKey(CereShopPrice record);

    List<ShopPriceWithRule> selectPriceList(List<Long> shopIdList);

    List<Product> selectProductListByPriceId(ShopPricePageParam param);

    List<ShopPriceDetail> getPrices(RenovationParam param);

    Long selectPriceByProductId(Long productId);
}
