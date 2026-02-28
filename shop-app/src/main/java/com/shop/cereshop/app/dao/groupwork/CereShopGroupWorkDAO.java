/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.groupwork;

import com.shop.cereshop.app.page.groupwork.GroupInvite;
import com.shop.cereshop.app.page.groupwork.GroupWorkIndex;
import com.shop.cereshop.app.page.groupwork.ShopGroupWorkUDetail;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.groupwork.GroupWorkParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopGroupWorkDAO extends BaseMapper<CereShopGroupWork> {
    int deleteByPrimaryKey(Long shopGroupWorkId);

    int insertSelective(CereShopGroupWork record);

    CereShopGroupWork selectByPrimaryKey(Long shopGroupWorkId);

    int updateByPrimaryKeySelective(CereShopGroupWork record);

    int updateByPrimaryKey(CereShopGroupWork record);

    GroupWorkIndex findShop(@Param("shopId") Long shopId);

    List<ToolProduct> findProducts(GroupWorkParam param);

    ProductDetail findBySkuId(@Param("skuId") Long skuId, @Param("shopGroupWorkId") Long shopGroupWorkId);

    Long findByProductId(@Param("skuId") Long skuId);

    GroupInvite findProductByOrderId(@Param("orderId") Long orderId);

    CereShopGroupWork findByCollageId(@Param("collageId") Long collageId);

    List<ShopGroupWorkUDetail> getGroupWorks(RenovationParam param);

    CereShopGroupWork selectByProductId(Long productId);
}
