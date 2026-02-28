/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.MemberProduct;
import com.shop.cereshop.app.param.member.MemberProductPageParam;
import com.shop.cereshop.commons.domain.product.CereProductMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereProductMemberDAO extends BaseMapper<CereProductMember> {

    int insertSelective(CereProductMember record);

    /**
     * 查询会员专区商品
     * @param param
     * @return
     */
    List<MemberProduct> getMemberProducts(MemberProductPageParam param);

    /**
     * 根据参数查找sku的会员价配置
     * @param memberLevelId
     * @param productId
     * @param skuId
     * @return
     */
    CereProductMember selectProductMember(@Param("memberLevelId") Long memberLevelId,
                                          @Param("productId") Long productId,
                                          @Param("skuId") Long skuId);

    List<CereProductMember> selectBatchProductMember(@Param("memberLevelId") Long memberLevelId,
                                                     @Param("skuIdList") List<Long> skuIdList);
}
