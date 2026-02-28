/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.member.CerePlatformMemberLevelDAO;
import com.shop.cereshop.app.dao.member.CerePlatformMembershipDAO;
import com.shop.cereshop.app.param.member.MemberLevelInfo;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CerePlatformMemberLevelServiceImpl implements CerePlatformMemberLevelService {

    @Autowired
    private CerePlatformMemberLevelDAO cerePlatformMemberLevelDAO;

    @Autowired
    private CerePlatformMembershipDAO cerePlatformMembershipDAO;

    @Override
    public CerePlatformMemberLevel selectByMemberLevelId(Long memberLevelId) {
        return cerePlatformMemberLevelDAO.selectByPrimaryKey(memberLevelId);
    }

    @Override
    public CerePlatformMemberLevel selectNextLevel(Long memberLevelId) {
        return cerePlatformMemberLevelDAO.selectNextLevel(memberLevelId);
    }

    @Override
    public CerePlatformMemberLevel selectFirstLevel() {
        return cerePlatformMemberLevelDAO.selectFirstLevel();
    }

    @Override
    public List<MemberLevelInfo> getAllMemberLevelInfo() {
        LambdaQueryWrapper<CerePlatformMemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CerePlatformMemberLevel::getGrowth);
        List<CerePlatformMemberLevel> memberLevelList = cerePlatformMemberLevelDAO.selectList(wrapper);
        List<MemberLevelInfo> levelInfoList = new ArrayList<>();
        for (CerePlatformMemberLevel level:memberLevelList) {
            MemberLevelInfo levelInfo = new MemberLevelInfo();
            BeanUtils.copyProperties(level, levelInfo);
            levelInfo.setMembershipList(cerePlatformMembershipDAO.selectByMemberLevelId(level.getMemberIds()));
            levelInfoList.add(levelInfo);
        }
        return levelInfoList;
    }


}
