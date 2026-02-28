/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.after;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.commons.domain.after.CereAfterProduct;
import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereAfterProductDAO extends BaseMapper<CereAfterProduct> {
    int insertSelective(CereAfterProduct record);

    List<CereAfterProductAttribute> findValuesBySkuId(@Param("skuId") Long skuId);

    List<CartSku> findSkusByAfterId(@Param("afterId") Long afterId);

    List<CereAfterProduct> findSkuBySkus(AfterParam param);

    List<CereAfterProduct> findByOrderIdForCheck(Long orderId);
}
