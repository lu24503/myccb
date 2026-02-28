/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.index;

import com.shop.cereshop.app.page.index.Index;
import com.shop.cereshop.app.param.index.IndexParam;
import com.shop.cereshop.app.param.index.SearchParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface IndexService {
    Index index(IndexParam param, CereBuyerUser user) throws CoBusinessException;

    List<CereBuyerSearch> getHistory(CereBuyerUser user) throws CoBusinessException;

    Page getSearchProducts(SearchParam param, CereBuyerUser user) throws CoBusinessException;

    void deleteSearch(SearchParam param) throws CoBusinessException;

    CerePlatformCanvas getCanvas(CerePlatformCanvas canvas) throws CoBusinessException;
}
