/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.compose;

import com.shop.cereshop.app.param.compose.CereShopComposeDTO;
import com.shop.cereshop.commons.domain.tool.CereComposeDetail;
import com.shop.cereshop.commons.domain.tool.CereComposeMergeInfo;
import com.shop.cereshop.commons.domain.tool.CereComposeProduct;
import com.shop.cereshop.commons.domain.tool.CereShopCompose;

import java.util.List;

public interface CereShopComposeService {

    CereShopCompose selectOnGoingByComposeId(Long composeId);

    List<CereComposeDetail> selectComposeByProductId(Long productId);

    List<CereShopComposeDTO> selectByShopIdList(List<Long> shopIdList);

    List<CereComposeMergeInfo> selectComposeByShopId(Long shopId);
}
