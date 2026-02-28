/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.log;

import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CerePlatformLogService {
    void addLog(CereBuyerUser user, String module, String operation, String describe, Long id, String time) throws CoBusinessException;
}
