/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product.impl;

import com.shop.cereshop.app.dao.product.CereCommentWordDAO;
import com.shop.cereshop.app.page.product.CommentWord;
import com.shop.cereshop.app.service.product.CereCommentWordService;
import com.shop.cereshop.commons.domain.product.CereCommentWord;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereCommentWordServiceImpl implements CereCommentWordService {

    @Autowired
    private CereCommentWordDAO cereCommentWordDAO;

    @Override
    public void insertBatch(List<CereCommentWord> list) throws CoBusinessException {
        cereCommentWordDAO.insertBatch(list);
    }

    @Override
    public List<CommentWord> findByProductId(Long productId) {
        return cereCommentWordDAO.findByProductId(productId);
    }
}
