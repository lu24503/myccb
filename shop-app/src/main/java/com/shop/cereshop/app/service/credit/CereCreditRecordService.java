/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.credit;


import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.credit.CereCreditRecord;

/**
 * <p>
 * 业务接口
 * 积分流水表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
public interface CereCreditRecordService {

    Page<CereCreditRecord> selectCreditRecord(Long buyerUserId, PageParam param);

}
