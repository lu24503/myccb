/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.cart;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.cart.ShopCart;
import com.shop.cereshop.commons.domain.cart.CereCartAttribute;
import com.shop.cereshop.commons.domain.cart.CereShopCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopCartDAO extends BaseMapper<CereShopCart> {
    int deleteByPrimaryKey(Long cartId);

    int insertSelective(CereShopCart record);

    CereShopCart selectByPrimaryKey(Long cartId);

    int updateByPrimaryKeySelective(CereShopCart record);

    int updateByPrimaryKey(CereShopCart record);

    CereShopCart findShopCart(@Param("buyerUserId") Long buyerUserId,@Param("skuId") Long skuId);

    List<CereCartAttribute> findAttributes(@Param("skuId") Long skuId);

    List<ShopCart> findCartByUserId(@Param("buyerUserId") Long buyerUserId);

    List<CartSku> findProductByShopId(@Param("shopId") Long shopId,@Param("buyerUserId") Long buyerUserId);

    void deleteByIds(@Param("ids") List<Long> ids,@Param("buyerUserId") Long buyerUserId);

    void deleteAll(@Param("buyerUserId") Long buyerUserId);

    void updateSelected(@Param("ids") List<Long> ids,@Param("buyerUserId") Long buyerUserId,@Param("selected") Integer selected);

    CereShopCart findByParam(@Param("shopId") Long shopId,@Param("skuId") Long skuId,@Param("buyerUserId") Long buyerUserId);

    void deleteSkus(@Param("carts") List<CartSku> carts,@Param("buyerUserId") Long buyerUserId);

    void updateSelectedAll(@Param("buyerUserId") Long buyerUserId);

    CereShopCart findSku(@Param("skuId") Long skuId);

    CereShopCart findBySkuIdAndUser(@Param("skuId") Long skuId,@Param("buyerUserId") Long buyerUserId);

    List<CartSku> findOrderProduct(@Param("orderId") Long orderId);

    void insertBatch(@Param("list") List<CereShopCart> list);

    void insertOrUpdate(CereShopCart record);

    void updateBatch(@Param("list") List<CereShopCart> list);

    Integer findNumber(@Param("buyerUserId") Long buyerUserId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    ActivityData findActivityData(@Param("skuId") Long skuId);

    Boolean clearInvalidSku(Long buyerUserId);
}
