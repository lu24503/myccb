/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.param.index.SearchParam;
import com.shop.cereshop.commons.domain.product.CereSkuMemberRealInfo;
import com.shop.cereshop.commons.domain.product.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereSkuMemberRealInfoDAO extends BaseMapper<CereSkuMemberRealInfo> {

    int insertOrUpdate(CereSkuMemberRealInfo memberRealInfo);

    int updateSkuPrice(CereSkuMemberRealInfo memberRealInfo);

    int deleteByProductId(Long productId);

    int deleteBySkuId(Long skuId);

    int deleteByProductIdList(List<Long> normalProductIdList);

    int increSalesVolumeBy(@Param("productId") Long productId,
                           @Param("addedNumber") Integer addedNumber);

    int updateSelective(@Param("realInfo") CereSkuMemberRealInfo skuRealInfo,
                        @Param("resetSalesVolume") boolean resetSalesVolume);

    int clearMemberProductInfo(Long skuId);

    ProductDetail findProductDetailBySkuId(@Param("skuId") Long skuId,
                                           @Param("shopId") Long shopId,
                                           @Param("memberLevelId") Long memberLevelId);

    List<Sku> findSkuListByProductId(@Param("productId") Long productId,
                                     @Param("memberLevelId") Long memberLevelId);

    List<Product> getSearchProducts(SearchParam param);

    int increSalesUserCount(List<Long> updateProductIdList);

    List<CartSku> findSkuListBySkuIdList(@Param("skuIdList") List<Long> skuIdList,
                                         @Param("memberLevelId") Long memberLevelId);

}
