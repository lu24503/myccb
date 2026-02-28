/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.member;

import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.member.CereMemberSigninRecord;

import java.util.List;

public interface CereMemberSigninRecordService {

    /**
     * 查询签到列表
     * @param buyerUserId
     * @return
     */
    List<CereMemberSigninRecord> selectSigninRecordList(Long buyerUserId);

    /**
     * 分页查询签到明细
     * @param pageParam
     * @param buyerUserId
     * @return
     */
    Page selectSigninHistory(PageParam pageParam, Long buyerUserId);

    /**
     * 签到
     * @param buyerUserId
     * @return
     */
    boolean signIn(Long buyerUserId);
}
