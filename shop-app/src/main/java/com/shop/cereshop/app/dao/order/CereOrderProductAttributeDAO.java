/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.order;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.commons.domain.order.CereOrderProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereOrderProductAttributeDAO extends BaseMapper<CereOrderProductAttribute> {

    int insertSelective(CereOrderProductAttribute record);

    List<CereOrderProductAttribute> findBySkuId(@Param("skuId") Long skuId);

    void insertBatch(@Param("list") List<OrderProductAttribute> list);

    List<OrderProductAttribute> findBySkus(@Param("skus") List<CartSku> skus);
}
