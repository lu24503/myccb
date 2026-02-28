/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.member;

import com.shop.cereshop.commons.domain.member.CerePlatformMembership;

import java.util.List;

public interface CerePlatformMembershipService {

    /**
     * 查询会员权益列表
     * @param memberLevelId
     * @return
     */
    List<CerePlatformMembership> selectByMemberLevelId(Long memberLevelId);
}
