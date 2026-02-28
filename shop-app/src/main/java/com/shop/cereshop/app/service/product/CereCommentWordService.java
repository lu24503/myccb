/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.page.product.CommentWord;
import com.shop.cereshop.commons.domain.product.CereCommentWord;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereCommentWordService {
    void insertBatch(List<CereCommentWord> collect) throws CoBusinessException;

    List<CommentWord> findByProductId(Long productId);
}
