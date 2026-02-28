/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.member.impl;

import com.shop.cereshop.app.dao.member.CerePlatformMemberLevelDAO;
import com.shop.cereshop.app.dao.member.CerePlatformMembershipDAO;
import com.shop.cereshop.app.service.member.CerePlatformMembershipService;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.member.CerePlatformMembership;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CerePlatformMembershipServiceImpl implements CerePlatformMembershipService {

    @Autowired
    private CerePlatformMemberLevelDAO cerePlatformMemberLevelDAO;

    @Autowired
    private CerePlatformMembershipDAO cerePlatformMembershipDAO;

    @Override
    public List<CerePlatformMembership> selectByMemberLevelId(Long memberLevelId) {
        CerePlatformMemberLevel memberLevel = cerePlatformMemberLevelDAO.selectByPrimaryKey(memberLevelId);
        if (memberLevel != null && StringUtils.isNotBlank(memberLevel.getMemberIds())) {
            return cerePlatformMembershipDAO.selectByMemberLevelId(memberLevel.getMemberIds());
        }
        return Collections.emptyList();
    }
}
