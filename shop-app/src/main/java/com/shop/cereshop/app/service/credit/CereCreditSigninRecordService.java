/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.credit;

import com.shop.cereshop.commons.domain.credit.CereCreditSigninRecord;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 积分签到表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
public interface CereCreditSigninRecordService {

    boolean signIn(Long buyerUserId);

    List<CereCreditSigninRecord> selectByMonth(Long buyerUserId, String month);
}
