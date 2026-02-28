/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.scene.impl;

import com.shop.cereshop.app.dao.scene.CereShopSceneMemberDAO;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberService;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereShopSceneMemberServiceImpl implements CereShopSceneMemberService {

    @Autowired
    private CereShopSceneMemberDAO cereShopSceneMemberDAO;

    @Override
    public CereShopSceneMember selectSceneMemberList(Long sceneId, Long memberLevelId) {
        return cereShopSceneMemberDAO.selectSceneMemberList(sceneId, memberLevelId);
    }
}
