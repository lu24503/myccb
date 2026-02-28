/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork;

import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereCollageOrderDetailService {
    void insert(CereCollageOrderDetail detail) throws CoBusinessException;

    void updateInvalid(List<Long> ids) throws CoBusinessException;

    CereCollageOrderDetail findKaituan(Long collageId);

    List<CollagePerson> findCollagePerson(Long collageId);

    List<CereCollageOrderDetail> findByOrderIdList(List<Long> orderIdList);
}
