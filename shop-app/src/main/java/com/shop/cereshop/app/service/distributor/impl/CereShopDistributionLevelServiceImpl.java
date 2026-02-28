/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.cereshop.app.dao.distributor.CereShopDistributionLevelDAO;
import com.shop.cereshop.app.page.distributor.LevelDescDTO;
import com.shop.cereshop.app.service.distributor.CereShopDistributionLevelService;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributionLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CereShopDistributionLevelServiceImpl implements CereShopDistributionLevelService {

    @Autowired
    private CereShopDistributionLevelDAO cereShopDistributionLevelDAO;

    @Override
    public List<LevelDescDTO> getDistributionLevelConfig(Long shopId) {
        List<CereShopDistributionLevel> levelList = cereShopDistributionLevelDAO.selectList(Wrappers.<CereShopDistributionLevel>lambdaQuery()
                .eq(CereShopDistributionLevel::getShopId, shopId)
                .orderByAsc(CereShopDistributionLevel::getLevelNum));
        if (CollectionUtils.isEmpty(levelList)) {
            return Collections.emptyList();
        }
        List<LevelDescDTO> dtoList = new ArrayList<>();
        for (CereShopDistributionLevel level:levelList) {
            LevelDescDTO dto = new LevelDescDTO();
            BeanUtils.copyProperties(level, dto);
            if (level.getIfMoney() == 0 && level.getIfCustomer() == 0 && level.getIfInvitation() == 0) {
                dto.setLevelDesc("无门槛直接参与推广分销, 直接分佣比例" + level.getDirectProportion() + "%, 间接分佣比例" + level.getIndirectProportion() + "%");
            } else {
                StringBuilder builder = new StringBuilder();
                if (level.getIfMoney() == 1 && level.getConditionMoney() != null) {
                    builder.append("累计直接分销金额满" + level.getConditionMoney() + "元,");
                }
                if (level.getIfInvitation() == 1 && level.getConditionInvitation() != null) {
                    builder.append("邀请下级满" + level.getConditionInvitation() + "人,");
                }
                if (level.getIfCustomer() == 1 && level.getConditionCustomer() != null) {
                    builder.append("客户满" + level.getConditionCustomer() + "人,");
                }
                builder.append("直接分佣比例" + level.getDirectProportion() + "%, " + "间接分佣比例" + level.getIndirectProportion() + "%");
                dto.setLevelDesc(builder.toString());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
}
