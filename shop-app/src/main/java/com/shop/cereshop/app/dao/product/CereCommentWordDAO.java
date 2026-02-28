/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.product;

import com.shop.cereshop.app.page.product.CommentWord;
import com.shop.cereshop.commons.domain.product.CereCommentWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereCommentWordDAO extends BaseMapper<CereCommentWord> {

    int insertSelective(CereCommentWord record);

    void insertBatch(@Param("list") List<CereCommentWord> list);

    List<CommentWord> findByProductId(@Param("productId") Long productId);
}
